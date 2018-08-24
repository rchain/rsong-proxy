package coop.rchain.api

import cats.effect._

import coop.rchain.service.UserService.UserService
import io.circe.syntax._

import org.http4s.circe._

import org.http4s._
import org.http4s.dsl.io._

class UserApi(svc: UserService) {

  val service = HttpService[IO] {

    case GET -> Root / id =>
      if (svc.find(id).isEmpty)
        NotFound(id)
      else
        Ok(svc.find(id).get.asJson)

    case req @ POST -> Root / id =>
      Ok(svc.newUser(id))

    case req @ PUT -> Root / id / "playcount" =>
      Accepted(svc.updatePlayCount(id = id, playCount = 100))
  }
}
