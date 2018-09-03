package coop.rchain.api

import cats.effect._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.Cursor
import io.circe.Json
import org.http4s.circe._
import coop.rchain.service._
import coop.rchain.protocol.Protocol._
import coop.rchain.repo._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import monix.eval.Task
import monix.execution.CancelableFuture
import coop.rchain.domain._
import org.http4s.dsl.io._
import org.http4s.headers._

class SongApi[F[_]: Sync]() extends Http4sDsl[F] {

  object perPage extends OptionalQueryParamDecoderMatcher[Int]("per_page")
  object page extends OptionalQueryParamDecoderMatcher[Int]("page")
  object userId extends QueryParamDecoderMatcher[String]("userId")

  //TODO pass these as you instantiate calss
  val songRepo = SongRepo()
  val userRepo = UserRepo()
  val svc = new SongService(SongRepo()) //TODO remove once we're off moc data
  val log = Logger("SongApi")

  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "song" :? userId(id) +& perPage(pp) +& page(p) =>
        Ok(svc.allSongs(id, Cursor(10, 1)).asJson)

      case GET -> Root / "song" / id :? userId(uid) =>
        val link = songRepo.cacheSong(id)
        link.fold(
          l => {
            log.error(s"error in caching song with id: $id.")
            log.error(s"${l}")
            InternalServerError()
          },
          r => TemporaryRedirect(Location(uri("/$r/")))
        )

      case GET -> Root / "artwork" / id ⇒
        Ok(Json.obj("message" -> Json.fromString("under construction")))
    }
}
