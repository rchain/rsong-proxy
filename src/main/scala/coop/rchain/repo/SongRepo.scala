package coop.rchain.repo

import java.io._
import com.typesafe.scalalogging.Logger
import coop.rchain.crypto.codec.Base16
import coop.rchain.domain.RSongModel.RSongAsset
import coop.rchain.utils.Globals._
import coop.rchain.utils.HexBytesUtil._
import coop.rchain.domain._
import coop.rchain.domain.RSongModel._
import scala.util._
import coop.rchain.models.Par
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

  def apply(proxy: RholangProxy): SongRepo =
    new SongRepo(proxy)
}

class SongRepo(proxy: RholangProxy) {

  import SongRepo._
  import Repo._

  val log = Logger[SongRepo]

  def asRholang(asset: RSongJsonAsset) = {
    log.info(s"-- name to retrieve song: ${asset.id}")

    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.jsonData}, "${asset.id}")"""
  }

  def deployNoPropose(asset: RSongJsonAsset) =
    (asRholang _
      andThen
        proxy.deployNoPropose _)(asset)

  def deployAndProposeAsset(asset: RSongJsonAsset) =
    (asRholang _
      andThen
        proxy.deployAndPropose _)(asset)

  def asRhoTerm(asset: RSongAsset) = {
    log.info(
      s"-- name to retrieve song: ${asset.rsong.isrc}-${asset.typeOfAsset}")

    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.rsong.asJson.toString}, "${asset.rsong.isrc}-${asset.typeOfAsset}")"""
  }

  def deployAndPropose(asset: RSongAsset) =
    (asRhoTerm _
      andThen
        proxy.deployAndPropose _)(asset)

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

  private val songMap: scala.collection.mutable.Map[String, Array[Byte]] =
    scala.collection.mutable.Map.empty[String, Array[Byte]]

  def findInBlock(assetName: String): Either[Err, Array[Byte]] = {
    log.info(s"in findInBlock. assetName = $assetName")
    val song = if (!songMap.contains(assetName)) {

      for {
        sid <- findByName(proxy, assetName)
        randSuffix = scala.util.Random.alphanumeric.take(20).toString()
        _ = log.info(s"sid: $sid")
        queryName = s"""("$sid".hexToBytes(),"${sid}-${SONG_OUT}")"""
        term = s"""@["Immersion", "retrieveSong"]!$queryName"""
        m <- proxy.deployAndPropose(term)
        p <- findByName(proxy, s"${sid}-${SONG_OUT}")
        ba = Base16.decode(p)
        _ = songMap.update(assetName, ba)
      } yield ba
    } else {
      log.info(s"Asset $assetName found in the map cache.")
      Right(songMap(assetName))
    }
    song match {
      case Right(s) =>
        log.info(s"got asset $assetName--")
        Right(s)
      case Left(e) =>
        log.error(s"asset retrieval error: ${e}")
        Left(e)
    }
  }

}
