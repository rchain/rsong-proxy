package coop.rchain.api

import org.http4s._
import org.http4s.implicits._
import org.specs2._
import cats.effect.IO
import jdk.nashorn.internal.ir.RuntimeNode
import cats.instances.all._
import scala.concurrent.Future
import cats.Applicative._
import cats.syntax.applicative._
import scala.concurrent.ExecutionContext.Implicits.global

class PlaySpec extends Specification { def is = s2""""
  Play API Specificaitons
    should return 200 for play by song-id  $e1
  """

  def e1 = {
    retSong.status.code must beEqualTo(200)
  }

  private[this] val retSong: Response[IO] = {
    val ff = List.empty[Int].pure[Future]
    val getSong = Request[IO](Method.GET, Uri.uri("/song?userId=user123&perPage=10&page=1"))
    new Play[IO].service.orNotFound(getSong).unsafeRunSync()
  }
}
