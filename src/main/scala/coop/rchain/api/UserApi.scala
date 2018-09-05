package coop.rchain.api

import cats.effect._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.repo._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserApi[F[_]: Sync](proxy: RholangProxy) extends Http4sDsl[F] {
  import Repo._

  val repo = UserRepo(proxy)
  val log = Logger("UserApi")
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / id =>
      findByName(proxy, id)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFound) {
              val _ = Future {
                repo.newUser(id)
              }
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
      val _ = Future {
        repo.newUser(id)
      }
      Accepted(
        User(id = id,
             name = None,
             active = true,
             lastLogin = System.currentTimeMillis,
             playCount = 100,
             metadata = Map("immersionUser" -> "ImmersionUser")).asJson)

    case req @ PUT -> Root / id / "playcount" =>
      Accepted()

    case req @ GET -> Root / id / "playcount" =>
      repo
        .findPlayCount(id)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFound) NotFound(s"${e}")
            else InternalServerError(s"${e.code} ; ${e.msg}"), //NotFound(id),
          r => Ok(r.asJson)
        )
  }

}
