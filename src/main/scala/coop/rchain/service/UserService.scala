package coop.rchain.service

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import cats.implicits._
import coop.rchain.models.Par
import coop.rchain.repo.RholangProxy
import coop.rchain.rholang.interpreter.PrettyPrinter
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import coop.rchain.repo.RholangProxy._
import coop.rchain.models.rholang.implicits._
import coop.rchain.protocol.Protocol._

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

  def newUserRhoTerm(name: String): String =
    s"""@["Immersion", "newUserId"]!("${name}")"""

  def apply(): UserService =
    new UserService(RholangProxy(host, port))
  def apply(grpc: RholangProxy): UserService =
    new UserService(grpc)

}
class UserService(grpc: RholangProxy) {
  import UserService._
  val log = Logger[UserService]

  val newUser: String => Either[Err, DeployAndProposeResponse] = user =>
    (newUserRhoTerm _ andThen grpc.deployAndPropose _)(user)

  def find(rName: String): Either[Err, String] =
    for {
      d <- dataAtNameAsPar(s""""${rName}"""")
      e <- dataAtName(d)
    } yield e

  def dataAtNameAsPar(term: String) =
    for {
      z <- grpc.dataAtName(term)
      pars = z.blockResults.flatMap(_.postBlockData)
    } yield pars

  def dataAtName(pars: Seq[Par]) = {
    val e = pars.map(p => PrettyPrinter().buildString(p))
    if (e.isEmpty)
      Left(Err(ErrorCode.nameNotFount, s"Rholang name not found${}", None))
    else Right(e.head)
  }

  import coop.rchain.protocol.ParOps._

  val computePlayCount: String => Either[Err, DeployAndProposeResponse] =
    userId =>
      for {
        rhoName <- find(userId)
        queryName = s"""("$rhoName".hexToBytes(),"${userId}-${COUNT_OUT}")"""
        term = s"""@["Immersion", "playCount"]!${queryName}"""
        m <- grpc.deployAndPropose(term)
      } yield m

  val findPlayCount: String => Either[Err, String] = userId =>
    find(s"$userId-$COUNT_OUT")

}
