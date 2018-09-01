package coop.rchain.api

import cats.effect._
import coop.rchain.domain.Cursor
import io.circe.Json
import org.http4s.circe._
import coop.rchain.service._
import coop.rchain.protocol.Protocol._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

class SongApi[F[_]: Sync](svc: SongService) extends Http4sDsl[F] {

  object perPage extends OptionalQueryParamDecoderMatcher[Int]("per_page")
  object page extends OptionalQueryParamDecoderMatcher[Int]("page")
  object userId extends QueryParamDecoderMatcher[String]("userId")

  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "song" :? userId(id) +& perPage(pp) +& page(p) =>
        Ok(svc.allSongs(id, Cursor(10, 1)).asJson)

      case GET -> Root / "song" / id :? userId(uid) =>
        val ret = svc.aSong(SongRequest(songId = id, userId = uid))
        ret match {
          case Some(song) => Ok(song.asJson)
          case None       => NotFound(id)
        }

      case GET -> Root / "artwork" / id ⇒
        Ok(Json.obj("message" -> Json.fromString("under construction")))
//      case GET -> Root / "song" / name =>
//        val songfile =
//          "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
//        Ok(svc.getSong(songfile + name))
    }
}
