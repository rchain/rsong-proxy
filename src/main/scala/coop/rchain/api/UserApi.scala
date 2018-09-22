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

class UserApi[F[_]: Sync](proxy: RholangProxy) extends Http4sDsl[F] {
  import UserRepo._

  val log = Logger("UserApi")
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / userId =>
      log.debug(s"GET / userId request form user: $userId")
      val cachedUser=RSongCache.getUser(userId)(proxy)
            Ok(
              User(id = userId,
                   name = None,
                   active = true,
                   lastLogin = System.currentTimeMillis,
                   playCount = cachedUser.playCount.current,
                   metadata = Map("immersionUser" -> "ImmersionUser")).asJson)

    case GET -> Root / id / "playcount" =>
      log.debug(s"GET / id /playcount request form user: $id")
        fetchPlayCount(id)(proxy)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFound) NotFound(s"${e}")
            else InternalServerError(s"${e.code} ; ${e.msg}"),
          r => Ok(r.asJson)
        )
  }

}
