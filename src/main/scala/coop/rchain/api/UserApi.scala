package coop.rchain.api

import cats.effect.{Effect, IO}
import coop.rchain.service.UserService
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class UserApi[F[_]: Effect] extends Http4sDsl[F] {

  val svc = UserService()

  implicit val userProto = jsonOf[F, coop.rchain.domain.User]

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root / id =>
        if (svc.find(id).isEmpty)
          NotFound(id)
        else
          Ok(svc.find(id).asJson)

      case req @ POST -> Root / id =>
        Ok(svc.newUser(id).asJson)

      case req @ PUT -> Root / id / "playcount" =>
        Accepted(svc.updatePlayCount(id = id, playCount = 100))
    }
  }
}
