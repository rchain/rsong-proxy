package coop.rchain.repo

import java.io._
import java.nio.file._
import java.util

import com.google.protobuf.ByteString
import io.circe.generic.auto._
import io.circe.syntax._
import com.typesafe.scalalogging.Logger
import coop.rchain.crypto.codec.Base16
import coop.rchain.domain.RSongModel.RSongAsset
import coop.rchain.utils.Globals._
import coop.rchain.utils.HexBytesUtil._
import coop.rchain.domain._
import coop.rchain.domain.RSongModel._

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

  def asRholang(asset: RSongJsonAsset) = {
    log.info(s"-- name to retrieve song: ${asset.id}")

    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.jsonData}, "${asset.id}")"""
  }

  def deployNoPropose(asset: RSongJsonAsset) =
    (asRholang _
      andThen
        grpc.deployNoPropose _)(asset)

  def deployAndProposeAsset(asset: RSongJsonAsset) =
    (asRholang _
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

  private val songMap: scala.collection.mutable.Map[String, Array[Byte]] =
    scala.collection.mutable.Map.empty[String, Array[Byte]]

  //songName isrc-Stereo or isrc-3D
  def findInBlock(assetName: String): Either[Err, Array[Byte]] = {

    val song = if (!songMap.contains(assetName)) {

      for {
        sid <- find(assetName)
        randSuffix = scala.util.Random.alphanumeric.take(20).toString()
        _ = println(s"sid: $sid")
        queryName = s"""("$sid".hexToBytes(),"${sid}-${SONG_OUT}")"""
        term = s"""@["Immersion", "retrieveSong"]!$queryName"""
        m <- grpc.deployAndPropose(term)
        p <- Repo.find(grpc)(s"${sid}-${SONG_OUT}")
        ba = Base16.decode(p)
        _ = songMap.update(assetName, ba)
      } yield ba
    } else {
      log.info(s"Asset $assetName found in the map cache.")
      Right(songMap(assetName))
    }
    song match {
      case Right(s) =>
        log.info(s"-- got asset $assetName--")
        Right(s)
      case Left(e) =>
        log.error(s"-- asset retrieval error: ${e}")
        Left(e)
    }
  }

  def find(rName: String): Either[Err, String] = Repo.find(grpc)(rName)

  def dataAtName(pars: Seq[Par]) = Repo.dataAtName(pars)

  def getMetadata(id: String) = {}

}
