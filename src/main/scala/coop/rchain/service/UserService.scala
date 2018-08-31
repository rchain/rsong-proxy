package coop.rchain.service

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import cats.implicits._
import coop.rchain.repo.RholangProxy
import coop.rchain.rholang.interpreter.PrettyPrinter
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import coop.rchain.repo.RholangProxy._
import coop.rchain.models.rholang.implicits._

object UserService {
  val COUNT_OUT = "countOut"
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

  def playCountRhoTerm(name: String): String =
    s"""@["Immersion", "playCount"]!("${name}", "${name}-${COUNT_OUT}")"""

  def newUserRhoTerm(name: String): String =
    s"""@["Immersion", "newUserId"]!("${name}")"""

  def apply(): UserService = new UserService(RholangProxy(host, port))
  def apply(grpc: RholangProxy): UserService = new UserService(grpc)

}
class UserService(grpc: RholangProxy) {
  import UserService._
  val log = Logger[UserService]

  val newUser: String => Either[Err, DeployAndProposeResponse] = user =>
    (newUserRhoTerm _ andThen grpc.deployAndPropose _)(user)

  def find(userId: String): Either[Err, String] = {
    val rName = s""""${userId}""""
    log.debug(s"searching for $rName")
    dataAtName(rName)
  }

  def dataAtName(term: String) = {
    for {
      z <- grpc.dataAtNameWithTerm(term)
      pars = z.blockResults.flatMap(_.postBlockData)
      e = pars.map(p => PrettyPrinter().buildString(p))
      rholangId <- if (e.isEmpty)
        Left(
          Err(ErrorCode.nameNotFount,
              s"Rholang name not fount for ${term}",
              None))
      else Right(e.head)
    } yield rholangId
  }

  val playCoutnAsk: String => Either[Err, String] =
    userId => {
      val rName = s""""${userId}""""
      for {
        z <- grpc.dataAtNameWithTerm(rName)
        userPars = z.blockResults.flatMap(_.postBlockData)
        userPar <- if (userPars.headOption.isDefined) Right(userPars.head);
        else
          Left(Err(ErrorCode.nameNotFount, s"no name found for $userId", None))
        coutPar <- asPar(COUNT_OUT)
        e <- grpc.dataAtName(coutPar ++ userPar)
        count = PrettyPrinter().buildString(e)
      } yield count
    }
  def updatePlayCount(userId: String, playCount: Int): Json = Json.obj()

}
