package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import scala.util._

object UserRepo {
  import Repo._
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

  val newUser: String => RholangProxy => Either[Err, DeployAndProposeResponse] = user => proxy =>
    (newUserRhoTerm _ andThen proxy.deployAndPropose _)(user)

  def putPlayCountAtName(
      userId: String,
      playCountOut: String)(proxy: RholangProxy): Either[Err, DeployAndProposeResponse] =
    for {
      rhoName <- findByName(proxy, userId)
      playCountArgs = s"""("$rhoName".hexToBytes(), "$playCountOut")"""
      term = s"""@["Immersion", "playCount"]!${playCountArgs}"""
      m <- proxy.deployAndPropose(term)
    } yield m

  def fetchPlayCount(userId: String)(proxy: RholangProxy): Either[Err, PlayCount] = {
    val playCountOut = s"$userId-${COUNT_OUT}-${System.currentTimeMillis()}"
    val pc = for {
      _ <- putPlayCountAtName(userId, playCountOut)(proxy)
      count <- findByName(proxy, playCountOut)
      countAsInt <- asInt(count)
    } yield PlayCount(countAsInt)
    log.info(s"userid: $userId has ${pc}")
    pc
  }

  def decPlayCount(songId: String, userId: String)(proxy: RholangProxy) = {
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
    pOut
  }
}
