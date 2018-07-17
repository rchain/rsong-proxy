package coop.rchain.api

import cats.effect.Effect
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._

class Play[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    object perPage extends OptionalQueryParamDecoderMatcher[Int] ("per_page")
    object page extends OptionalQueryParamDecoderMatcher[Int] ("page")
    object userId extends QueryParamDecoderMatcher[String] ("user_id")
    HttpService[F] {
      case GET -> Root  / "user" :? userId(id) +& perPage(pp) +& page (p) =>
        Ok(
          Json.obj(
            "user" -> Json.fromString(s" ${id}"),
            "per_page" -> Json.fromString(s" ${pp.getOrElse(10)}"),
            "page" -> Json.fromString(s" ${p.getOrElse(0)}")))
      case GET -> Root  / id ⇒
        Ok(Json.obj("play_id" -> Json.fromString(s"${id}")))
    }
  }
}
