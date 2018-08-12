package coop.rchain.api

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.specs2._

class PlaySpec extends Specification { def is = s2""""
  Play API Specificaitons
    should return 200 for play by song-id  $e1
  """

  def e1 = retSong.status.code must beEqualTo(200)


  private[this] val retSong: Response[IO] = {
    val getSong = Request[IO](Method.GET, Uri.uri("/song/song-123?userId=user123&perPage=10&page=1"))
    new Play[IO].service.orNotFound(getSong).unsafeRunSync()
  }
}
