package coop.rchain

import cats.effect._

import org.http4s.server.blaze.BlazeBuilder
import cats.syntax.all._
import api.{SongApi, Status, UserApi}
import utils.Globals._
import coop.rchain.service._
import coop.rchain.repo._

object Bootstrap extends IOApp {

  import scala.concurrent.ExecutionContext.Implicits.global

  import api.middleware._

  val songService: SongService = SongService(SongRepo())

  val apiVersion = appCfg.getString("api.version")

  def statusApi = new Status().service

  def userApi = new UserApi(UserService()).service

  def songApi = new SongApi(songService).service

  def run(args: List[String]): IO[ExitCode] = {

    BlazeBuilder[IO]
      .bindHttp(appCfg.getInt("api.http.port"), "0.0.0.0")
      .mountService(MiddleWear(statusApi))
      .mountService(MiddleWear(statusApi), s"/${apiVersion}/public")
      .mountService(MiddleWear(userApi), s"/${apiVersion}/user")
      .mountService(MiddleWear(songApi), s"/${apiVersion}")
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
