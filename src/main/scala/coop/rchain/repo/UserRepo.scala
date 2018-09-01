package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.models.Par
import coop.rchain.rholang.interpreter.PrettyPrinter
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

object UserRepo {
  val COUNT_OUT = "countOut"
  val logger = Logger[UserRepo.type]

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

  def apply(): UserRepo =
    new UserRepo(RholangProxy(host, port))
  def apply(grpc: RholangProxy): UserRepo =
    new UserRepo(grpc)
}

class UserRepo(grpc: RholangProxy) {

  import UserRepo._
  val log = Logger[UserRepo]

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
