package coop.rchain.repo

import java.io._
import java.nio.file._

import io.circe.generic.auto._
import io.circe.syntax._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.RSongModel.RSongAsset
import coop.rchain.utils.Globals._
import coop.rchain.utils.HexBytesUtil._
import coop.rchain.domain._

import scala.util._
import coop.rchain.models.Par
import coop.rchain.rholang.interpreter.PrettyPrinter
import coop.rchain.utils.HexBytesUtil
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

object SongRepo {
  val rsongPath = appCfg.getString("api.http.rsong.path")
  val SONG_OUT = "SONG-OUT"
  val threshold = appCfg.getInt("rsongdata.concat.size")

  def logDepth(s: String): String = {
    if (s.length <= threshold)
      s""""$s""""
    else {
      val mid = s.length / 2
      val l = logDepth(s.substring(0, mid))
      val r = logDepth(s.substring(mid))
      s"""(\n$l\n++\n$r\n)"""
    }
  }

  def loadSongFile(fileName: String) = {
    val bis = new BufferedInputStream(new FileInputStream(fileName))
    Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
  }

  private lazy val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))

  def apply(): SongRepo =
    new SongRepo(RholangProxy(host, port))

  def apply(grpc: RholangProxy): SongRepo =
    new SongRepo(grpc)
}

class SongRepo(grpc: RholangProxy) {

  import SongRepo._

  val log = Logger[SongRepo]

  def asRhoTerm1(asset: RSongJsonAsset) = {
    log.info(s"-- name to retrieve song: ${asset.id}-${asset.typeOfAsset}")

    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.jsonData}, "${asset.id}-${asset.typeOfAsset}")"""
  }

  def deployAndPropose1(asset: RSongJsonAsset) =
    (asRhoTerm1 _
      andThen
        grpc.deployAndPropose _)(asset)

  def asRhoTerm(asset: RSongAsset) = {
    log.info(
      s"-- name to retrieve song: ${asset.rsong.isrc}-${asset.typeOfAsset}")

    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.rsong.asJson.toString}, "${asset.rsong.isrc}-${asset.typeOfAsset}")"""
  }

  def deployAndPropose(asset: RSongAsset) =
    (asRhoTerm _
      andThen
        grpc.deployAndPropose _)(asset)

  def asHexConcatRsong(file: String): Either[Err, String] = {
    Try {
      (loadSongFile _
        andThen bytes2hex _
        andThen logDepth)(file)
    } match {
      case Success(s) =>
        Right(s)
      case Failure(e) =>
        Left(
          Err(ErrorCode.rsongHexConversion,
              s"${e.getMessage}",
              Some(s"fileName= ${file}")))
    }
  }

  def songFromBlock(name: String) = {
    val asRhoTerm =
      s"""@["Immersion", "retrieveSong"]!(${name}, "songDataOut")"""
    log.debug(s"retrieveSong name= ${name}")
    grpc.deployAndPropose(asRhoTerm)
  }

  //songName isrc-Stereo or isrc-3D
  def retrieveSong(songName: String): Either[Err, Array[Byte]] = {
    val songAsString = for {
      sid <- find(songName)
      _ = log.info(s"--- songAsString ${sid}")
      queryName = s"""("$sid".hexToBytes(),"${sid}-${SONG_OUT}")"""
      _ = log.info(s"--- queryName= ${queryName}")
      term = s"""@["Immersion", "retrieveSong"]!${queryName}"""
      _ = log.info(s"--- term = ${term}")
      m <- grpc.deployAndPropose(term)
      p <- Repo.find(grpc)(s"${sid}-${SONG_OUT}")
    } yield p
    songAsString match {
      case Right(s) =>
        log.info(s"-- got song = ${s}")
        Right(HexBytesUtil.hex2bytes(s))
      case Left(e) =>
        log.error(s"-- retreiveSong error: ${e}")
        Left(e)
    }
  }

  def find(rName: String): Either[Err, String] = Repo.find(grpc)(rName)

  def dataAtName(pars: Seq[Par]) = Repo.dataAtName(pars)

  def storeFile(fileName: String,
                songData: Array[Byte]): Either[Err, String] = {

    var out: Option[FileOutputStream] = None
    Try {
      out = Some(new FileOutputStream(fileName))
      out.get.write(songData)
    } match {
      case Success(_) =>
        out.get.close
        Right(fileName)
      case Failure(e) =>
        out.get.close
        Left(Err(ErrorCode.errorInCachingSong, e.getMessage, None))
    }
  }

  def writeSongToCache(name: String): Either[Err, String] = {
    for {
      b <- retrieveSong(name)
      s <- storeFile(s"${rsongPath}/${name}", b)
    } yield (s)
  }

  // sotre song by isrc & type: isrc-Sterio or isrc-3D
  def cacheSong(name: String): Either[Err, String] = {
    val fileName = s"${rsongPath}/${name}"
    val file = new File(fileName)
    if (file.exists()) {
      log.info(s"--- file ${fileName} already exist")
      Right((s"${rsongPath}/${name}"))
    } else {
      log.info(s"file is not in cache. going to fetch file: ${fileName}")
      writeSongToCache(name)
    }
  }

}
