package coop.rchain.api.middleware

import cats.effect.{Effect, IO}
import cats.implicits._
import org.http4s._
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.http4s.server.middleware._
import scala.concurrent.duration._
import org.http4s.headers.Authorization
import org.http4s.util._

object MiddleWear {
  def addHeader[F[_]: Effect](resp: Response[F], header: Header) =
    resp match {
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp => resp
    }

    val methodConfig = CORSConfig(
      anyOrigin = true,
      anyMethod = false,
      exposedHeaders = Set("*").some, 
      allowedMethods = Set("GET", "POST").some,
      allowCredentials = true,
      allowedOrigins = Set("*"),
      maxAge = 1.day.toSeconds)

  def apply[F[_]: Effect](service: HttpService[F]) = {
    val s = CORS(service, methodConfig) 
    s.map(addHeader(_, Header("XX-header", "XX-value") ) )
  } 
}
