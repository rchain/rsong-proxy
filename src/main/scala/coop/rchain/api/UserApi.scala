package coop.rchain.api

import cats.effect._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.{Err, ErrorCode}
import coop.rchain.repo.UserRepo
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class UserApi[F[_]: Sync](repo: UserRepo) extends Http4sDsl[F] {

  val log = Logger("UserApi")
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / id =>
      repo
        .find(id)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFount) NotFound(s"${e}")
            else InternalServerError(s"${e.code} ; ${e.msg}"), //NotFound(id),
          r => Ok(Json.obj(id -> Json.fromString(r)))
        )

    case req @ POST -> Root / id =>
      repo
        .newUser(id)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFount) NotFound(s"${e}")
            else InternalServerError(s"${e.code} ; ${e.msg}"), //NotFound(id),
          r => Ok(Json.obj(id -> Json.fromString("is created")))
        )

    case req @ PUT -> Root / id / "playcount" =>
      Accepted(Json.obj("status" -> Json.fromString("under construction")))
  }

}
