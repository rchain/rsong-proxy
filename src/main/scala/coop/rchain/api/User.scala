package coop.rchain.api

import cats.effect.Effect
import coop.rchain.service.UserService
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import coop.rchain.utils.Globals._
import io.circe.generic.auto._
import io.circe.syntax._
import coop.rchain.service.UserService._

class User[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root  / id =>
        Ok(find(id).asJson)

      case req @ POST -> Root  =>
        Ok(req.body.drop(6))
    }
  }
}
