package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain.NameKey
import org.specs2._
import coop.rchain.utils.Globals._
import coop.rchain.service._
import org.specs2.matcher.MatchResult

class RholangProxyITSpec extends Specification {
//  def is = s2"""
//   Rnode Specification
//     create new contract form file $deployContract
//     add user $addUser
//     show black mush show changens $ok//showBlocks
//     show data at contract names $ok//getUser
//     compute playcount  $ok//computePlayCount
//     fetch playcount $ok//findUserPlayCount
//"""
  
  val log = Logger[RholangProxyITSpec]

  val userService = UserRepo(proxy)
  val userName = "john-smith1"

  def is = s2"""
                Rnode specification it should work $deploySeq
    """

  def deploySeq = {
    deployContract
    addUser
  }

  def deployContract: MatchResult[Boolean] = {
   val result = proxy.deployFromFile("/rho/immersion.rho")
    log.info(s"contract creation completed with result: ${result}")
    result.isRight === true
  }

  def addUser: MatchResult[Boolean] = {
    log.info(s"adding user: $userName")
    val result = userService.newUser(userName)
    log.info(s"useradd completed with result: ${result}")
    result.isRight === true
  }

  private def getUser:MatchResult[Boolean] = {
    log.info(s"attempting the listenAtName: ${userName}")
    val results = Repo.findByName(proxy, userName)
    log.info(s"grpcResults of name-retrivals = ${results}")
    (results.isRight && !results.toOption.get.isEmpty) === true
  }

  private def findUserPlayCount = {
    log.info(s"fetch user playcount for userId : ${userName}")
    val results = userService.fetchPlayCount(userName)
    log.info(s"FETCH playcount rsult ret from playCOuntAsk: ${results}")
    results.isRight === true
  }
}

