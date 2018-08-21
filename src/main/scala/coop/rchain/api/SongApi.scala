package coop.rchain.api

import cats.effect.Effect
import coop.rchain.model.Cursor
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import coop.rchain.service.SongService._
import io.circe.generic.auto._
import io.circe.syntax._


class SongApi[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    object perPage extends OptionalQueryParamDecoderMatcher[Int] ("per_page")
    object page extends OptionalQueryParamDecoderMatcher[Int] ("page")
    object userId extends QueryParamDecoderMatcher[String] ("userId")

    HttpService[F] {
      case GET -> Root  / "song" :? userId(id) +& perPage(pp) +& page(p) =>
        Ok(mySongs(id, Cursor(10,1)).asJson)

      case GET -> Root  / "song" / id  :? userId(uid)  =>
        Ok(mySongs(id, Cursor(10,1)).head.asJson)



      case GET -> Root  / "artwork" / id ⇒
        Ok(Json.obj("message" -> Json.fromString("under construction")))

    }
  }
}
