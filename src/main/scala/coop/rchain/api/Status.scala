package coop.rchain.api

import cats.effect._
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class Status[F[_]: Sync] extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok(Json.obj("status" -> Json.fromString(s"up")))
    case GET -> Root / "status" =>
      Ok(Json.obj("status" -> Json.fromString(s"up")))
  }
}
