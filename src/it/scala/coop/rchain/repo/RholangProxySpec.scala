package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain.NameKey
import org.specs2._
import coop.rchain.utils.Globals._
import coop.rchain.service._
import org.specs2.matcher.MatchResult

class RholangProxySpec extends Specification {
  def is = s2"""
   Rnode Specification
     create new contract form fle $ok//deployContract
     add user $ok//addUser
     show black mush show changens $ok//showBlocks
     show data at contract names $ok//getUser
     playcount ask $ok//computePlayCount

  val log = Logger[RholangProxySpec]
  val host = appCfg.getString("grpc.host")
  val proxy = RholangProxy("localhost", 40401)
  val userService = UserService(proxy)
  val userName="john-smith"


  def deployContract = {

   val result = proxy.deployFromFile("/rho/immersion.rho")
    log.info(s"contract creation completed with result: ${result}")
    result.isRight === true
  }

  def showBlocks = {
    val blocks = proxy.showBlocks.map(_.toByteString.toStringUtf8)
    log.info(s"blocks size: ${blocks.size}")
//    log.info(s"show-blocks output: ${blocks}")
    blocks.isEmpty ===  false
  }

  def addUser ={
    log.info(s"adding user: ${userName}")
    val results = userService.newUser(userName)
    log.info(s"useradd completed with result: ${results}")
    results.isLeft === false
  }


  def getUser:MatchResult[Boolean] = {
    log.info(s"attempting the listenAtName: ${userName}")
    val results = userService.find(userName)
    log.info(s"grpcResults of name-retrivals = ${results}")
    (results.isRight && !results.toOption.get.isEmpty) === true
  }

  def computePlayCount = {
    log.info(s"asking for playcount for userId : ${userName}")
    val results = userService.computePlayCount(userName)
    log.info(s"rsult ret from playCOuntAsk: ${results}")
    results.isRight === true
  }

  def findUserPlayCount = {
    log.info(s"fetch user playcount for userId : ${userName}")
    val results = userService.findPlayCount(userName)
    log.info(s"FETCH playvount rsult ret from playCOuntAsk: ${results}")

    results.isRight === true
  }
}

