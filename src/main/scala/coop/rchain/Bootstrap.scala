package coop.rchain

import cats.effect.{Effect, IO}
import fs2.StreamApp
import org.http4s.server.blaze.BlazeBuilder
import scala.concurrent.ExecutionContext
import api.{SongApi, Status, UserApi}
import utils.Globals._
import coop.rchain.service._
import coop.rchain.repo._

object Bootstrap extends StreamApp[IO] {
  import scala.concurrent.ExecutionContext.Implicits.global
  def stream(args: List[String], requestShutdown: IO[Unit]) =
    ServerStream.stream[IO]
}

object ServerStream {
  import api.middleware._
  val songRepo: SongRepo = SongRepo()
  val songService: SongService = SongService(songRepo)

  val apiVersion = appCfg.getString("api.version")
  def statusApi[F[_]: Effect] = new Status[F].service
  def userApi[F[_]: Effect] = new UserApi[F].service
  def songApi[F[_]: Effect] =   new SongApi[F](songService).service

  def stream[F[_]: Effect](implicit ec: ExecutionContext) =
    BlazeBuilder[F]
      .bindHttp(appCfg.getInt("api.http.port"), "0.0.0.0")
      .mountService(MiddleWear(statusApi))
      .mountService(MiddleWear(statusApi), s"/${apiVersion}/public")
      .mountService(MiddleWear(userApi), s"/${apiVersion}/user" )
      .mountService(MiddleWear(songApi), s"/${apiVersion}" )
      .serve
}
