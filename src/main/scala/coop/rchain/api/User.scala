package coop.rchain.api

import cats.effect.Effect
import coop.rchain.service.UserService
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import coop.rchain.utils.Globals._
import io.circe.generic.auto._
import io.circe.syntax._
import coop.rchain.service.UserService._

class User[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root  / id =>
        if( find(id).isEmpty )
          NotFound(id)
        else
        Ok(find(id).asJson)

      case GET -> Root  / "asset" / "re" => PermanentRedirect("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")

      case req @ PUT -> Root / id / "playcount"  =>
        req.decode[UrlForm] { data =>
          data.values.get("count") match {
            case Some(Seq(s, _*)) =>
              val count = s.split(' ').filter(_.length > 0).map(_.trim.toInt).sum
              Ok(count.toString)
            case None => BadRequest(s"Invalid data: " + data)
          }
        }

      case req @ POST -> Root  =>
        Ok(req.body.drop(6))
    }
  }
}
