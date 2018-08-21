package coop.rchain.api

import cats.effect.{Effect, IO}
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._
import coop.rchain.service.UserService._
import io.circe.generic.auto._
import cats.effect.Effect
import cats.syntax.flatMap._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class User[F[_]: Effect] extends Http4sDsl[F] {

  implicit val de = jsonOf[F, UserPC]

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root  / id =>
        if( find(id).isEmpty )
          NotFound(id)
        else
        Ok(find(id).asJson)

      case GET -> Root  / "asset" / "re" => PermanentRedirect("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")


      case req @ PUT  -> Root / "playcount"  =>
         req.as[UserPC].flatMap(p => Accepted(updatePlayCount(p).asJson))
    }
  }
}
