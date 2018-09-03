package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.protocol.Protocol
import coop.rchain.service.SongService
import coop.rchain.service.moc.RSongData
import io.circe.generic.auto._
import coop.rchain.utils.HexBytesUtil._
import io.circe.syntax._
import org.specs2._

class SongRepoSpec extends Specification {
  def is = s2"""
   Song Service Specification
      retrieve a cursor compliant list of available songs $e1
      retrieve a song $e2
      JSON RSong protocol $rSongJson
      cache the binary filelll $cacheRsong
"""
  val log = Logger[SongRepoSpec]
  val repo = SongRepo()
  val svc = SongService(repo)

  def e2 = {
    val req = Protocol.SongRequest(songId = "song-123", userId = "user-123")
    val computed = svc.aSong(req)
    log.debug(s"computed-songs = ${computed}")
    computed.toString.isEmpty === false
  }

  def e1 = {
    val computed = svc.allSongs("userid", Cursor(10, 1))
    log.debug(s"computed-songs = ${computed}")
    (computed.headOption.isDefined) === true
  }

  def rSongJson = {
    val rsongMetadata = RSongData.rsongMetaData.asJson
    log.info(s"rosongMetadat: ${rsongMetadata}")

    //TODO pending completion
    1 === 1
  }
  def cacheRsong = {
    val buf = hex2bytes("e04fd020ea3a6910a2d808002b30309d")
    repo.cacheSong("test-bin-file", buf)
    1 === 1

  }

}
