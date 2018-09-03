package coop.rchain.api

import cats.effect._
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

class SongApi[F[_]: Sync]() extends Http4sDsl[F] {

  object perPage extends OptionalQueryParamDecoderMatcher[Int]("per_page")
  object page extends OptionalQueryParamDecoderMatcher[Int]("page")
  object userId extends QueryParamDecoderMatcher[String]("userId")

  //TODO pass these as you instantiate calss
  val songRepo = SongRepo()
  val userRepo = UserRepo()
  val svc = new SongService(SongRepo()) //TODO remove once we're off moc data

  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "song" :? userId(id) +& perPage(pp) +& page(p) =>
        Ok(svc.allSongs(id, Cursor(10, 1)).asJson)

      case GET -> Root / "song" / id :? userId(uid) =>
        val nameStereo = s"${id}-Stereo"
        val name3D = s"${id}-3D"

        val link3DTask = Task.eval(songRepo.cacheSong(nameStereo))
        val linkStereoTask = Task.eval(songRepo.cacheSong(name3D))
        val playCountTask = Task.eval(userRepo.findPlayCount(uid))
        Task.zip3(link3DTask, linkStereoTask, playCountTask).map {
          case (a, b, c) => "it worked"
        }

        val ret = svc.aSong(SongRequest(songId = id, userId = uid))
        ret match {
          case Some(song) => Ok(song.asJson)
          case None       => NotFound(id)
        }

      case GET -> Root / "artwork" / id ⇒
        Ok(Json.obj("message" -> Json.fromString("under construction")))
    }
}
