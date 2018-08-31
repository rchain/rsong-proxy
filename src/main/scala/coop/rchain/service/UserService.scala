package coop.rchain.service

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.models.Par
import coop.rchain.repo.RholangProxy
import coop.rchain.rholang.interpreter.PrettyPrinter
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

object UserService {
  val COUNT_OUT = "playCountOut"
  val logger = Logger[UserService.type]

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

class UserService(proxy: RholangProxy) {
  import UserService._
  val log = Logger[UserService]

  val newUser: String => Either[Err, DeployAndProposeResponse] = user =>
    (newUserRhoTerm _ andThen proxy.deployAndPropose _)(user)

  def find(rName: String): Either[Err, String] =
    for {
      d <- dataAtNameAsPar(s""""${rName}"""")
      e <- dataAtName(d)
    } yield e

  def dataAtNameAsPar(term: String) =
    for {
      z <- proxy.dataAtName(term)
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
        m <- proxy.deployAndPropose(term)
      } yield m

  val findPlayCount: String => Either[Err, String] = userId =>
    find(s"$userId-$COUNT_OUT")

}
