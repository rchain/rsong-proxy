package coop.rchain.service

import io.circe.generic.auto._, io.circe.syntax._
import coop.rchain.repo.SongRepo
import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.protocol.Protocol
import coop.rchain.domain.RSongModel
import org.specs2._

class SongServiceSpec extends Specification {
  def is = s2"""
   Song Service Specification
      retrieve a cursor compliant list of available songs $e1
      retrieve a song $e2
      JSON RSong protocol $rSongJson
"""
  val log = Logger[SongServiceSpec]
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

}
