package coop.rchain.api

import cats.effect._
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s._
import org.http4s.dsl.io._

class Status[F[_]: Sync] extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root =>
      Ok(Json.obj("status" -> Json.fromString(s"up")))
    case req @ GET -> Root / "status" =>
      Ok(Json.obj("status" -> Json.fromString(s"up")))
  }
}
