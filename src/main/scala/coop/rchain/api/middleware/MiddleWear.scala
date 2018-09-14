package coop.rchain.api.middleware

import cats.effect._
import cats.implicits._
import org.http4s._
import scala.concurrent.duration._
import org.http4s.server.middleware.{CORS, CORSConfig}
import org.http4s.util.CaseInsensitiveString

object MiddleWear {

  def addHeader[F[_]: Effect](resp: Response[F], header: Header) =
    resp match {
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp                    => resp
    }
  def addBinHeader[F[_]: Effect](resp: Response[F], header: Header) =
    resp match {
      case Status.Successful(resp) =>
        resp.putHeaders(
          header,
          Header("Content-Type", "binary/octet-stream"),
          Header("Accept-Ranges", "bytes")
        )
      case resp => resp
    }

  val methodConfig = CORSConfig(
    anyOrigin = true,
    anyMethod = false,
    allowedMethods = Set("GET", "POST", "HEAD", "OPTIONS", "PATCH").some,
    allowedHeaders = Some(
      Set("Origin",
          "X-Requested-With",
          "Content-Type",
          "Accept",
          "Authorization")),
    allowCredentials = true,
    allowedOrigins = Set("*"),
    maxAge = 1.day.toSeconds
  )

  def corsHeader[F[_]: Effect](service: HttpService[F]) = {
    val s = CORS(service, methodConfig)
    s.map(addHeader(_, Header("Server", "RSong")))
  }

  def binHeader[F[_]: Effect](service: HttpService[F]) = {
    val s = CORS(service, methodConfig)
    s.map(addBinHeader(_, Header("Server", "RSong")))
  }

  def bearierToken[F[_]: Effect](req: Request[F]): Option[String] =
    req.headers.get(CaseInsensitiveString("authorization")).map(_.value)
}
