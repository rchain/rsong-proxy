package coop.rchain.api

import cats.effect._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.{Err, ErrorCode, User}
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
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserApi[F[_]: Sync]() extends Http4sDsl[F] {

  lazy val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))
  println(s"userAPI using host rnode : ${host}")
  val proxy = RholangProxy(host, port)
  val repo = UserRepo(proxy)

  val log = Logger("UserApi")
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / id =>
      repo
        .find(id)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFount) {
              val _ = Future { repo.newUser(id) }
              Ok(
                User(id = id,
                     name = None,
                     active = true,
                     lastLogin = System.currentTimeMillis,
                     playCount = 100,
                     metadata = Map("immersionUser" -> "ImmersionUser")).asJson)
            } else
              InternalServerError(s"${e.code} ; ${e.msg}"), //NotFound(id),
          r =>
            Ok(
              User(id = id,
                   name = None,
                   active = true,
                   lastLogin = System.currentTimeMillis,
                   playCount = 100,
                   metadata = Map("immersionUser" -> "ImmersionUser")).asJson)
        )

    case req @ POST -> Root / id =>
      val _ = Future { repo.newUser(id) }
      Accepted(
        User(id = id,
             name = None,
             active = true,
             lastLogin = System.currentTimeMillis,
             playCount = 100,
             metadata = Map("immersionUser" -> "ImmersionUser")).asJson)
//      repo
//        .newUser(id)
//        .fold(
//          e =>
//            if (e.code == ErrorCode.nameNotFount) NotFound(s"${e}")
//            else InternalServerError(s"${e.code} ; ${e.msg}"), //NotFound(id),
//          r => Ok(Json.obj(id -> Json.fromString("is created")))
//        )

    case req @ PUT -> Root / id / "playcount" =>
      Accepted()

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
