package coop.rchain.api

import cats.effect._
import coop.rchain.domain.Cursor
import io.circe.Json
import org.http4s.circe._
import coop.rchain.service._
import coop.rchain.domain.Protocol._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class SongApi[F[_]: Sync](svc: SongService) extends Http4sDsl[F] {

  object perPage extends OptionalQueryParamDecoderMatcher[Int]("per_page")
  object page extends OptionalQueryParamDecoderMatcher[Int]("page")
  object userId extends QueryParamDecoderMatcher[String]("userId")

  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "song" :? userId(id) +& perPage(pp) +& page(p) =>
        Ok(svc.mySongs(Cursor(10, 1)))

      case GET -> Root / "song" / id :? userId(uid) =>
        Ok(svc.mySong(SongRequest(id, uid)))

      case GET -> Root / "artwork" / id ⇒
        Ok(Json.obj("message" -> Json.fromString("under construction")))
      case GET -> Root / "song" / name =>
        val songfile =
          "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
        Ok(svc.getSong(songfile + name))
    }
}
