package coop.rchain.api

import cats.effect._
import io.circe.Json
import org.http4s.circe._
import org.http4s._
import org.http4s.dsl.io._

import coop.rchain.domain.Cursor
import io.circe.Json
import coop.rchain.service.SongService._
import coop.rchain.service._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.Protocol._

class SongApi(svc: SongService) {

  object perPage extends OptionalQueryParamDecoderMatcher[Int]("per_page")
  object page extends OptionalQueryParamDecoderMatcher[Int]("page")
  object userId extends QueryParamDecoderMatcher[String]("userId")

  val service = HttpService[IO] {

    case GET -> Root / "song" :? userId(id) +& perPage(pp) +& page(p) =>
      Ok(svc.mySongs(Cursor(10, 1)))

    case GET -> Root / "song" / id :? userId(uid) =>
      Ok(svc.mySong(SongRequest(id, uid)))

    case GET -> Root / "artwork" / id ⇒
      Ok(Json.obj("message" -> Json.fromString("under construction")))
  }
}
