package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.models.Par
import coop.rchain.rholang.interpreter.PrettyPrinter
import io.circe._
import io.circe.generic.auto._
import scala.util._
import io.circe.syntax._

object UserRepo {
  val COUNT_OUT = "COUNT-OUT"
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

  def asInt(s: String): Either[Err, Int] = {
    Try(s.toInt) match {
      case Success(i) => Right(i)
      case Failure(e) =>
        Left(Err(ErrorCode.playCountConversion, e.getMessage, None))
    }
  }

  def apply(): UserRepo =
    new UserRepo(RholangProxy(host, port))

  def apply(grpc: RholangProxy): UserRepo =
    new UserRepo(grpc)
}

class UserRepo(grpc: RholangProxy) {

  import UserRepo._
  val log = Logger[UserRepo]

  val songRepo = SongRepo(grpc)

  val newUser: String => Either[Err, DeployAndProposeResponse] = user =>
    (newUserRhoTerm _ andThen grpc.deployAndPropose _)(user)

  def find(rName: String): Either[Err, String] = Repo.find(grpc)(rName)

  def dataAtNameAsPar(term: String): Either[Err, Seq[Par]] =
    Repo.dataAtNameAsPar(grpc)(term)

  val computePlayCount: String => Either[Err, DeployAndProposeResponse] =
    userId =>
      for {
        rhoName <- find(userId)
        queryName = s"""("$rhoName".hexToBytes(),"${userId}-${COUNT_OUT}")"""
        term = s"""@["Immersion", "playCount"]!${queryName}"""
        m <- grpc.deployAndPropose(term)
      } yield m

  def findPlayCount(userId: String): Either[Err, Int] =
    for {
      c <- computePlayCount(userId)
      c <- Repo.find(grpc)(s"$userId-$COUNT_OUT")
      i <- asInt(c)
    } yield (i)

}
