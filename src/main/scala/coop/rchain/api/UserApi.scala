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
  import RSongUserCache._

  val log = Logger("UserApi")
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / userId =>
      log.debug(s"GET / userId request form user: $userId")
      getOrCreateUser(userId)(proxy).fold(
        l => {
          log.error(s"Error in ROOT/$userId api. ${l}")
          InternalServerError(s"${l.code} ; ${l.msg}")
        },
          r =>
            Ok(
              User(id = userId,
                   name = None,
                   active = true,
                   lastLogin = System.currentTimeMillis,
                   playCount = r.playCount.current,
                   metadata = Map("immersionUser" -> "ImmersionUser")).asJson)
      )
    case GET -> Root / id / "playcount" =>
      log.debug(s"GET / id /playcount request form user: $id")
        getOrCreateUser(id)(proxy)
        .fold(
          e =>
            if (e.code == ErrorCode.nameNotFound) NotFound(s"${e}")
            else InternalServerError(s"${e.code} ; ${e.msg}"),
          r => Ok(r.playCount.asJson)
        )
  }

}
