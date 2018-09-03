package coop.rchain

import cats.effect._
import cats.implicits._

import org.http4s.server.blaze.BlazeBuilder
import api._
import utils.Globals._
import service._
import repo._
import scala.concurrent.ExecutionContext.Implicits.global
import api.middleware._

object Bootstrap extends IOApp {

  def run(args: List[String]) =
    ServerStream.stream[IO].compile.drain.as(ExitCode.Success)

}
object ServerStream {
  val apiVersion = appCfg.getString("api.version")

  def songService = new SongService(SongRepo())
  def statusApi[F[_]: Effect] = new Status[F].routes
  def userApi[F[_]: Effect] = new UserApi[F](UserRepo()).routes
  def songApi[F[_]: Effect] = new SongApi[F]().routes

  def stream[F[_]: ConcurrentEffect] =
    BlazeBuilder[F]
      .bindHttp(appCfg.getInt("api.http.port"), "0.0.0.0")
      .mountService(MiddleWear(statusApi))
      .mountService(MiddleWear(statusApi), s"/${apiVersion}/public")
      .mountService(MiddleWear(userApi), s"/${apiVersion}/user")
      .mountService(MiddleWear(songApi), "/${apiVersion}")
      .serve
}
