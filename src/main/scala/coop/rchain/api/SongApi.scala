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
import coop.rchain.service.moc.MocSongMetadata.mocSongs
import coop.rchain.utils.Globals.appCfg
import org.http4s.dsl.io._
import org.http4s.headers._
import org.http4s.Uri

class SongApi[F[_]: Sync]() extends Http4sDsl[F] {

  object perPage extends OptionalQueryParamDecoderMatcher[Int]("per_page")
  object page extends OptionalQueryParamDecoderMatcher[Int]("page")
  object userId extends QueryParamDecoderMatcher[String]("userId")

  lazy val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))
  val proxy = RholangProxy(host, port)

  val songRepo = SongRepo(proxy)

  val userRepo = UserRepo()
  val svc = new SongService(SongRepo()) //TODO remove once we're off moc data
  val log = Logger("SongApi")
  log.info(s"host= ${host}")

  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "song" :? userId(id) +& perPage(pp) +& page(p) =>
        Ok(mocSongs.values.toList.asJson)

      case GET -> Root / "song" / id :? userId(uid) =>
        val link = songRepo.findInBlock(id)
        link.fold(
          l => {
            log.error(s"error in finding asset by id: $id.")
            log.error(s"${l}")
            InternalServerError()
          },
          r => Ok(r)
        )

      case GET -> Root / "art" / id ⇒
        val link = songRepo.findInBlock(id)
        link.fold(
          l => {
            log.error(s"error in finding asset by id: $id.")
            log.error(s"${l}")
            InternalServerError()
          },
          r => Ok(r)
        )
    }
}
