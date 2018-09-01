package coop.rchain.repo

import java.io.{BufferedInputStream, FileInputStream}

import coop.rchain.domain._

object SongRepo {

  def apply(): SongRepo = new SongRepo()
}
class SongRepo {
  import SongRepo._
  import coop.rchain.utils.HexBytesUtil._

//  val songMetadataList: Cursor => List[SongMetadata] = cursor => mocSongs
//  val songMetadata: String => SongMetadata = id => mocSongs.head

  def loadSongFile(fileName: String) = {
    val bis = new BufferedInputStream(new FileInputStream(fileName))
    Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
  }

//  val loadToBase16: String => String = fileName =>
//    (loadSongFile _ andThen bytes2hex)(fileName)

  def storeSong(songData: Array[Byte]) = {

    """
      |@["Immersion", "store"]!(
      |  "<songdata>".hexToBytes(),
      |  {"artist": "Bee Gees", ...},
      |  "songId"
      |)
    """.stripMargin
  }
}
