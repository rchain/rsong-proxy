package coop.rchain.api.middleware

import cats.data.{Kleisli, OptionT}
import cats.effect.{Effect, IO}
import cats.implicits._
import coop.rchain.domain.Domain
import org.http4s._
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.http4s.server.middleware._

import scala.concurrent.duration._
import org.http4s.headers.Authorization
import org.http4s.util._

object MiddleWear {
  def myMiddle(service: HttpService[IO], header: Header): HttpService[IO] = cats.data.Kleisli { req: Request[IO] =>
    service(req).map {
      case Status.Successful(resp) =>
        resp.putHeaders(header)
      case resp  => resp
  }
  }


  def addHeader[F[_]: Effect](resp: Response[F], header: Header) =
    resp match {
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp  => resp
    }


    val methodConfig = CORSConfig(
      anyOrigin = true,
      anyMethod = false,
      allowedMethods = Set("GET", "POST", "HEAD", "OPTIONS", "PATCH").some,
      allowedHeaders = Some(Set("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization")),
      allowCredentials = true,
      allowedOrigins = Set("*"),
      maxAge = 1.day.toSeconds)

  def apply[F[_]: Effect](service: HttpService[F]) = {
    val s = CORS(service, methodConfig) 
    s.map(addHeader(_, Header("XX-header", "XX-value") ) )

  } 

  def bearierToken[F[_]: Effect](req: Request[F]): Option[String] =
    req.headers.get(CaseInsensitiveString("authorization")).map(_.value)
}
