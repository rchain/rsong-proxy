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
  
  def getBinaryData(
      assetName: String): Either[Err, Array[Byte]] =
    for {
      songId <- findByName(proxy, assetName)
      songIdOut = s"${songId}-${SONG_OUT}"
      retrieveSongArgs = s"""("$songId".hexToBytes(), "$songIdOut")"""
      termToRetrieveSong = s"""@["Immersion", "retrieveSong"]!$retrieveSongArgs"""
      _ <- proxy.deployAndPropose(termToRetrieveSong)
      songData <- findByName(proxy, songIdOut)
      binarySongData = Base16.decode(songData)
    } yield binarySongData

}
