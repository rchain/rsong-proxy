package coop.rchain.repo

import java.io.{BufferedInputStream, FileInputStream}

import coop.rchain.protocol.RSongModel.RSongAsset
import coop.rchain.domain._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import ImmersionNames._
import com.google.protobuf.ByteString
import com.typesafe.scalalogging.Logger
import coop.rchain.utils.Globals._

object SongRepo {

  private val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))

  def apply(): SongRepo =
    new SongRepo(RholangProxy(host, port))

  def apply(proxy: RholangProxy): SongRepo =
    new SongRepo(proxy)

}
class SongRepo(proxy: RholangProxy) {
  import SongRepo._
  import coop.rchain.utils.HexBytesUtil._
  val log = Logger[SongRepo]

  def loadSongFile(fileName: String) = {
    val bis = new BufferedInputStream(new FileInputStream(fileName))
    Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
  }

  def asTermToStoreSong(asset: RSongAsset) = {
    log.info(s"asTermToStoreSong received : ${asset.rsong}")
    s"""@["Immersion", "store"]!("${ByteString.copyFrom(asset.audioData)}", ${asset.rsong.asJson.toString}, "${asset.rsong.isrc}-${asset.audioType}")"""
  }

  def toRnode(asset: RSongAsset) =
    (asTermToStoreSong _
      andThen
        proxy.deployAndPropose _)(asset)

  def storeSong(songData: Array[Byte]) = {

    """
      |@["Immersion", "store"]!(
      |  "<songdata>".hexToBytes(),
      |  {"artist": "Bee Gees", ...},
      |  "songId"
      |)
    """.stripMargin
  }
  def builder = {}

}
