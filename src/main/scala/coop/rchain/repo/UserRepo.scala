package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.models.Par
import io.circe._
import io.circe.generic.auto._
import scala.util._
import io.circe.syntax._
import coop.rchain.utils.Globals._

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

  def newUserRhoTerm(name: String): String =
    s"""@["Immersion", "newUserId"]!("${name}")"""

  def asInt(s: String): Either[Err, Int] = {
    Try(s.toInt) match {
      case Success(i) => Right(i)
      case Failure(e) =>
        Left(Err(ErrorCode.playCountConversion, e.getMessage, None))
    }
  }

  def apply(grpc: RholangProxy): UserRepo =
    new UserRepo(grpc)
}

class UserRepo(grpc: RholangProxy) {
  import Repo._
  import UserRepo._

  val log = Logger[UserRepo]

  val songRepo = SongRepo(grpc)

  val newUser: String => Either[Err, DeployAndProposeResponse] = user =>
    (newUserRhoTerm _ andThen grpc.deployAndPropose _)(user)

  def dataAtNameAsPar(term: String): Either[Err, Seq[Par]] =
    Repo.getDataAtName(grpc, term)

  val computePlayCount: String => Either[Err, DeployAndProposeResponse] =
    userId =>
      for {
        rhoName <- findByName(grpc, userId)
        queryName = s"""("$rhoName".hexToBytes(),"${userId}-${COUNT_OUT}")"""
        term = s"""@["Immersion", "playCount"]!${queryName}"""
        m <- grpc.deployAndPropose(term)
      } yield m

  def findPlayCount(userId: String): Either[Err, PlayCount] =
    for {
      c <- computePlayCount(userId)
      c <- findByName(grpc, s"$userId-$COUNT_OUT")
      i <- asInt(c)
    } yield (PlayCount(i))

}
