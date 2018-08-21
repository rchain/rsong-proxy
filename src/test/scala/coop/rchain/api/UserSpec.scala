package coop.rchain.api

import io.circe._, io.circe.parser._, io.circe.generic.semiauto._
import cats.effect._
import cats.instances.all._
import scala.concurrent.Future
import cats.syntax.applicative._
import com.typesafe.scalalogging.Logger
import org.http4s._
import org.http4s.dsl.io._


import scala.concurrent.ExecutionContext.Implicits.global

import org.specs2._

class UserSpec extends Specification {
  def is =
    s2""""
  User API Specificaitons
    should return newly generated user object  $e1
  """

  val log = Logger[UserSpec]
  val api = new UserApi[IO].service
  implicit val userDecoder: Decoder[coop.rchain.model.User] = deriveDecoder
  implicit val userEncoder: Encoder[coop.rchain.model.User] = deriveEncoder

  def e1 = {
    val createUserObject = Request[IO](Method.POST, Uri.uri("/user-1234"))
    val computed = api.orNotFound(createUserObject).unsafeRunSync()
    val c = computed.body.compile.toVector.unsafeRunSync().toArray
    val userSting = new String(c, java.nio.charset.StandardCharsets.UTF_8)
    val user = for {
      j <- parse(userSting)
      u <- j.as[coop.rchain.model.User]
    } yield u
    user.toOption.isDefined === true
    user.toOption.get.id === "user-1234"
    retSong.status.code must beEqualTo(200)
  }


  private[this] val retSong: Response[IO] = {
    val ff = List.empty[Int].pure[Future]
    val getSong = Request[IO](Method.GET, Uri.uri("/song?userId=user123&perPage=10&page=1"))
    new SongApi[IO].service.orNotFound(getSong).unsafeRunSync()
  }
}
