package coop.rchain.repo

import java.io.{BufferedInputStream, FileInputStream}

import io.circe.generic.auto._
import io.circe.syntax._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.RSongModel.RSongAsset
import coop.rchain.utils.Globals._
import coop.rchain.utils.HexBytesUtil._
import coop.rchain.domain._
import scala.util._

object SongRepo {

  val threshold = appCfg.getInt("rsongdata.concat.size")
  def logDepth(s: String): String = {
    if (s.length <= threshold) s""""$s""""
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

  private val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))

  def apply(): SongRepo =
    new SongRepo(RholangProxy(host, port))

  def apply(proxy: RholangProxy): SongRepo =
    new SongRepo(proxy)
}

class SongRepo(proxy: RholangProxy) {
  import SongRepo._
  val log = Logger[SongRepo]

  def asRhoTerm(asset: RSongAsset) = {
    s"""@["Immersion", "store"]!(${asset.audioData}, ${asset.rsong.asJson.toString}, "${asset.rsong.isrc}-${asset.audioType}")"""
    log.info(
      s"-- name to retrieve song: ${asset.rsong.isrc}-${asset.audioType}")

    s"""@["Immersion", "store"]!(${asset.audioData}, ${asset.rsong.asJson.toString}, "${asset.rsong.isrc}-${asset.audioType}")"""
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
  def songFromBlock(name: String) = {
    val asRhoTerm =
      s"""@["Immersion", "retrieveSong"]!(${name}, "songDataOut")"""
    log.info(s"retrieveSong name= ${name}")
    proxy.deployAndPropose(asRhoTerm)
  }
  // songfile, metadata
//
//  def withRsong(rsong: RSong, file: String, ) = {
//    for {
//      f <- asHexConcatRsong(file))
//      b <- RsongAsse
//
//    }
//  }

//  def  upload ( file: String, rsong: Rsong) = {
//
//    for {
//      c<- asHexConcatRsong(file)
//      b <-
//    }
//
//  }
}
