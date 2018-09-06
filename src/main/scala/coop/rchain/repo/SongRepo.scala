package coop.rchain.repo

import java.io._
import com.typesafe.scalalogging.Logger
import coop.rchain.crypto.codec.Base16
import coop.rchain.domain.RSongModel.RSongAsset
import coop.rchain.utils.Globals._
import coop.rchain.utils.HexUtil._
import coop.rchain.domain._
import coop.rchain.domain.RSongModel._
import scala.util._

object SongRepo {
  val rsongPath = appCfg.getString("api.http.rsong.path")
  val SONG_OUT = "SONG-OUT"
  val threshold = appCfg.getInt("rsongdata.concat.size")

  private def logDepth(s: String): String = {
    if (s.length <= threshold)
      s""""$s""""
    else {
      val mid = s.length / 2
      val l = logDepth(s.substring(0, mid))
      val r = logDepth(s.substring(mid))
      s"""(\n$l\n++\n$r\n)"""
    }
  }

  private def readFileAsByteArray(fileName: String): Array[Byte] = {
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
    log.info(s"name to retrieve song: ${asset.id}")
    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.jsonData}, "${asset.id}")"""
  }

  def deployNoPropose(
      asset: RSongJsonAsset): Either[Err, DeployAndProposeResponse] =
    (asRholang _
      andThen
        proxy.deployNoPropose _)(asset)

  def deployAndProposeAsset(
      asset: RSongJsonAsset): Either[Err, DeployAndProposeResponse] =
    (asRholang _
      andThen
        proxy.deployAndPropose _)(asset)

  def asHexConcatRsong(filePath: String): Either[Err, String] = {
    Try {
      (readFileAsByteArray _
        andThen bytes2hex _
        andThen logDepth)(filePath)
    } match {
      case Success(s) =>
        Right(s)
      case Failure(e) =>
        Left(
          Err(ErrorCode.rsongHexConversion,
              s"${e.getMessage}",
              Some(s"fileName is ${filePath}")))
    }
  }

  private val songMap: scala.collection.mutable.Map[String, Array[Byte]] =
    scala.collection.mutable.Map.empty[String, Array[Byte]]

  def fetchSong(assetName: String): Either[Err, Array[Byte]] = {
    log.debug(s"in findInBlock. assetName = $assetName")
    val song = if (songMap.contains(assetName)) {
      fetchCachedSong(assetName)
    } else {
      fetchSongFromRnode(assetName)
    }
    song match {
      case Right(s) =>
        Right(s)
      case Left(e) =>
        log.error(s"asset retrieval error: ${e}")
        Left(e)
    }
  }

  private def fetchCachedSong(assetName: String): Either[Err, Array[Byte]] = {
    log.info(s"Asset $assetName found in the map cache.")
    Right(songMap(assetName))
  }

  private def fetchSongFromRnode(
      assetName: String): Either[Err, Array[Byte]] = {
    for {
      songId <- findByName(proxy, assetName)
      _ = log.info(s"sid: $songId")
      songIdOut = s"${songId}-${SONG_OUT}"
      retrieveSongArgs = s"""("$songId".hexToBytes(), "$songIdOut")"""
      termToRetrieveSong = s"""@["Immersion", "retrieveSong"]!$retrieveSongArgs"""
      _ <- proxy.deployAndPropose(termToRetrieveSong)

      songData <- findByName(proxy, songIdOut)
      binarySongData = Base16.decode(songData)
      _ = songMap.update(assetName, binarySongData)
    } yield binarySongData
  }
}
