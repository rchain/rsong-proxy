package coop.rchain.service

import cats.effect.{Effect, IO}
import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import cats.implicits._
import coop.rchain.repo.RholangProxy
import coop.rchain.rholang.interpreter.PrettyPrinter
import coop.rchain.utils.IdGen
import io.circe.Json
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

  object Rho {
    val nameUser = """"userId""""
    val namePlayCount = """"playCount""""
  }

}
class UserService(grpc: RholangProxy) {
  import UserService._
  val log = Logger[UserService]

  def newUser() = {
    val userId = s"a${(java.util.UUID.randomUUID.toString)}" //.filterNot(_ == '-')
    val addUser = s"""@["Immersion", "newUserId"]!("${userId}")"""
    log.info(s"adduser rho : ${addUser}")
    for {
      u <- grpc.deployAndPropse(addUser)
      _ = log.info(s"created user : ${u}")
      z <- grpc.dataAtNameWithTerm(s""""${userId}"""")
      _ = log.info(s"searched for the user=a${userId} -- ${z}")
      pars = z.blockResults.flatMap(_.postBlockData)
      e = pars.map(p => PrettyPrinter().buildString(p))
    } yield e
  }

  def updatePlayCount(userId: String, playCount: Int): Json = Json.obj()
  def find(id: String): Option[Json] = None

}
