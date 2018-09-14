package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.crypto.codec.Base16
import coop.rchain.domain._
import scala.util._

object SongRepo {
  val SONG_OUT = "SONG-OUT"

  def apply(proxy: RholangProxy): SongRepo =
    new SongRepo(proxy)
}

class SongRepo(proxy: RholangProxy) {

  import SongRepo._
  import Repo._

  val log = Logger[SongRepo]

  def asRholang(asset: RSongJsonAsset) = {
    log.debug(s"name to retrieve song: ${asset.id}")
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

  private val songMap: scala.collection.mutable.Map[String, Array[Byte]] =
    scala.collection.mutable.Map.empty[String, Array[Byte]]

  def fetchSong(assetName: String): Either[Err, Array[Byte]] = {
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
    Right(songMap(assetName))
  }

  private def fetchSongFromRnode(
      assetName: String): Either[Err, Array[Byte]] = {
    for {
      songId <- findByName(proxy, assetName)
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
