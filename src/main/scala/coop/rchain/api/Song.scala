package coop.rchain.api

import cats.effect.Effect
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class Song[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root  / id =>
        Ok(Json.obj("message" -> Json.fromString(s"song, ${id}")))

      case req @ POST -> Root  =>
        Ok(req.body.drop(6)) 
    }
  }
}
