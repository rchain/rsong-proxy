package coop.rchain.api

import cats.effect._
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s._
import org.http4s.dsl.io._

class Status {

  val service = HttpService[IO] {
    case req @ GET -> Root =>
      Ok(Json.obj("status" -> Json.fromString(s"up")))
    case req @ GET -> Root / "status" =>
      Ok(Json.obj("status" -> Json.fromString(s"up")))
  }
}
