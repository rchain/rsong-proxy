package coop.rchain.service

import coop.rchain.repo.SongRepo
import coop.rchain.domain.Protocol
import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import org.specs2._

class SongServiceSpec extends Specification { def is = s2"""
   Song Service Specification
      retrieve a cursor compliant list of available songs $e1
      retrieve a song $e2
"""
  val log = Logger[SongServiceSpec]
  val repo = SongRepo()
  val svc = SongService(repo)

  def e2 = {
    val req = Protocol.SongRequest(songId="song-123", userId="user-123")
    val computed = svc.mySong(req)
    log.debug(s"computed-songs = ${computed}")
    computed.toString.isEmpty === false
  }

  def e1 =  {
    val computed = svc.mySongs(Cursor(10,1))
    log.debug(s"computed-songs = ${computed}")
    (computed.asArray.isDefined && computed.asArray.get.size > 1) === true
  }

}
