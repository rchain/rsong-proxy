package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import io.circe._
import io.circe.generic.auto._
import scala.util._
import io.circe.syntax._

object UserRepo {
  val COUNT_OUT = "COUNT-OUT"
  val logger = Logger[UserRepo.type]

  def newUser(id: String): Json = {
    User(id = id,
         name = Some("Immersion-user"),
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

  def putPlayCountAtName(
      userId: String,
      playCountOut: String): Either[Err, DeployAndProposeResponse] =
    for {
      rhoName <- findByName(grpc, userId)
      playCountArgs = s"""("$rhoName".hexToBytes(), $playCountOut)"""
      term = s"""@["Immersion", "playCount"]!${playCountArgs}"""
      m <- grpc.deployAndPropose(term)
    } yield m

  def fetchPlayCount(userId: String): Either[Err, PlayCount] = {
    val now = System.currentTimeMillis()
    val playCountOut = s"$userId-$COUNT_OUT-$now"
    for {
      _ <- putPlayCountAtName(userId, playCountOut)
      count <- findByName(grpc, playCountOut)
      countAsInt <- asInt(count)
    } yield PlayCount(countAsInt)
  }

  // TODO: Call @["Immersion", "play"]!(...)
  def incPlayCount(userId: String): Unit {

  //TODO under development
  }
}
