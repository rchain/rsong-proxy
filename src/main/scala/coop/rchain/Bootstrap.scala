package coop.rchain

import cats.effect.{Effect, IO}
import fs2.StreamApp
import org.http4s.server.blaze.BlazeBuilder
import scala.concurrent.ExecutionContext
import api.{Song, Status, User}
import utils.Globals._

object Bootstrap extends StreamApp[IO] {
  import scala.concurrent.ExecutionContext.Implicits.global
  def stream(args: List[String], requestShutdown: IO[Unit]) =
    ServerStream.stream[IO]
}

object ServerStream {

  import api.middleware._

  val apiVersion = appCfg.getString("api.version")
  def statusApi[F[_]: Effect] = new Status[F].service
  def userApi[F[_]: Effect] = new User[F].service
  def songApi[F[_]: Effect] =   new Song[F].service

  def stream[F[_]: Effect](implicit ec: ExecutionContext) =
    BlazeBuilder[F]
      .bindHttp(appCfg.getInt("api.http.port"), "0.0.0.0")
      .mountService(MiddleWear(statusApi), s"/${apiVersion}/public")
      .mountService(MiddleWear(userApi), s"/${apiVersion}" )
      .mountService(MiddleWear(songApi), s"/${apiVersion}" )
      .serve
}
