package coop.rchain.api

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.circe._
import coop.rchain.service.UserService.UserService
import org.http4s.dsl.Http4sDsl

class UserApi[F[_]: Sync](svc: UserService) extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / id =>
      if (svc.find(id).isEmpty)
        NotFound(id)
      else
        Ok(svc.find(id).get)
    case req @ POST -> Root / id =>
      Ok(svc.newUser(id))
    case req @ PUT -> Root / id / "playcount" =>
      Accepted(svc.updatePlayCount(id = id, playCount = 100))
  }
}
