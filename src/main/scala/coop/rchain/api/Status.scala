package coop.rchain.api

import cats.effect.Effect
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class Status[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case req @ GET -> Root =>
        Ok(Json.obj("status" -> Json.fromString(s"up")))
      case req @ GET -> Root / "status" =>
        Ok(Json.obj("status" -> Json.fromString(s"up")))
    }
  }
}
