package coop.rchain.service

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import cats.implicits._
import coop.rchain.repo.RholangProxy
import coop.rchain.rholang.interpreter.PrettyPrinter
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

object UserService {
  val logger = Logger[UserService.type]

  def newUser(id: String): Json = {
    User(id = id,
         name = None,
         active = true,
         lastLogin = System.currentTimeMillis,
         playCount = 100,
         Map("key-1" -> "value-1", "key-2" -> "value-2")).asJson
  }

  import coop.rchain.utils.Globals._
  private val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))

  def apply(): UserService = new UserService(RholangProxy(host, port))
  def apply(grpc: RholangProxy): UserService = new UserService(grpc)

}
class UserService(grpc: RholangProxy) {
  import UserService._
  val log = Logger[UserService]

  def termBilder(name: String): String = {
    val term = s"""@["Immersion", "newUserId"]!("${name}")"""
    log.info(s"term = ${term}")
    term
  }

  val newUser: String => Either[Err, DeployAndProposeResponse] = user =>
    (termBilder _ andThen grpc.deployAndPropose _)(user)

  def find(userId: String): Either[Err, String] =
    for {
      z <- grpc.dataAtNameWithTerm(s""""${userId}"""")
      pars = z.blockResults.flatMap(_.postBlockData)
      e = pars.map(p => PrettyPrinter().buildString(p))
      rholangId <- if (e.isEmpty)
        Left(
          Err(ErrorCode.nameNotFount,
              s"Rholang name not fount for ${userId}",
              None))
      else Right(e.head)
    } yield rholangId

  def updatePlayCount(userId: String, playCount: Int): Json = Json.obj()

}
