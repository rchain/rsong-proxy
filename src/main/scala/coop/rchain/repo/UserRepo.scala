package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.repo.SongRepo.SONG_OUT
import io.circe._
import io.circe.generic.auto._

import scala.util._
import io.circe.syntax._

object UserRepo {
  val COUNT_OUT = "COUNT-OUT"
  val logger = Logger[UserRepo.type]

  def newUserRhoTerm(name: String): String =
    s"""@["Immersion", "newUserId"]!("${name}")"""

  def asInt(s: String): Either[Err, Int] = {
    Try(s.toInt) match {
      case Success(i) => Right(i)
      case Failure(e) =>
        Left(Err(ErrorCode.playCountConversion, e.getMessage, None))
    }
  }

  def apply(proxy: RholangProxy): UserRepo =
    new UserRepo(proxy)
}

class UserRepo(proxy: RholangProxy) {

  import Repo._
  import UserRepo._

  val newUser: String => Either[Err, DeployAndProposeResponse] = user =>
    (newUserRhoTerm _ andThen proxy.deployAndPropose _)(user)

  def putPlayCountAtName(
      userId: String,
      playCountOut: String): Either[Err, DeployAndProposeResponse] =
    for {
      rhoName <- findByName(proxy, userId)
      playCountArgs = s"""("$rhoName".hexToBytes(), "$playCountOut")"""
      term = s"""@["Immersion", "playCount"]!${playCountArgs}"""
      m <- proxy.deployAndPropose(term)
    } yield m

  def fetchPlayCount(userId: String): Either[Err, PlayCount] = {
    val playCountOut = s"$userId-${COUNT_OUT}-${System.currentTimeMillis()}"
    val pc = for {
      _ <- putPlayCountAtName(userId, playCountOut)
      count <- findByName(proxy, playCountOut)
      countAsInt <- asInt(count)
    } yield PlayCount(countAsInt)
    log.info(s"++++++++++++++++userid: $userId has ${pc}")
    pc
  }

  // TODO: Call @["Immersion", "play"]!(...)
  def decPlayCount(songId: String, userId: String): Unit = {
    val permittedOut=s"${userId}-${songId}-permittedToPlay-${System.currentTimeMillis()}"
    val pOut = for {
      sid <- findByName(proxy, s"${songId}_Stereo.izr")
      _=log.info(s"rholangName= $sid for songId: $songId")
      uid <-  findByName(proxy, userId)
      _=log.info(s"rholangName= $uid for userId: $userId")
      parameters = s"""("$sid".hexToBytes(), "$uid".hexToBytes(), "$permittedOut")"""
      term = s"""@["Immersion", "play"]!${parameters}"""
      m <- proxy.deployAndPropose(term)
      p <- findByName(proxy, permittedOut)
    } yield p

    log.info(s"user: $userId with song: $songId has permitedOut: $pOut")

    //TODO under development
  }
}
