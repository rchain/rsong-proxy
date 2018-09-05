package coop.rchain.api

import cats.effect._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.{Err, ErrorCode}
import coop.rchain.repo.{RholangProxy, SongRepo, UserRepo}
import coop.rchain.utils.Globals.appCfg
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

class UserApi[F[_]: Sync]() extends Http4sDsl[F] {

  lazy val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))
  val proxy = RholangProxy(host, port)
  val repo = UserRepo(proxy)

  val log = Logger("UserApi")
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / id =>
      repo
        .find(id)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFount) NotFound(s"${id}")
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

    case req @ GET -> Root / id / "playcount" =>
      repo
        .findPlayCount(id)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFount) NotFound(s"${e}")
            else InternalServerError(s"${e.code} ; ${e.msg}"), //NotFound(id),
          r => Ok(r.asJson)
        )
  }

}
