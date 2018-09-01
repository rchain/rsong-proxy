package coop.rchain.repo

import java.io.{BufferedInputStream, FileInputStream}

import io.circe.generic.auto._
import io.circe.syntax._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.RSongModel.RSongAsset
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

  val log = Logger[SongRepo]

  def loadSongFile(fileName: String) = {
    val bis = new BufferedInputStream(new FileInputStream(fileName))
    Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
  }

  def asRhoTerm(asset: RSongAsset) =
    s"""@["Immersion", "store"]!("${asset.audioData}", ${asset.rsong.asJson.toString}, "${asset.rsong.isrc}-${asset.audioType}")"""

  def deployAndPropose(asset: RSongAsset) =
    (asRhoTerm _
      andThen
        proxy.deployAndPropose _)(asset)

}
