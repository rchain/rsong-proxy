package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import org.specs2._
import coop.rchain.utils.Globals._
import coop.rchain.service._
import org.specs2.matcher.MatchResult

class RholangProxySpec extends Specification {
  def is =
    s2"""
   Rnode Specification
     create new contract form file $deployContract
     show black mush show changens $showBlocks
     add user $ok//addUser
     show data at contract names $ok //dataAtName
    """

  val log = Logger[RholangProxySpec]
  val host = appCfg.getString("grpc.host")
  val proxy = RholangProxy("localhost", 40401)

  val contractNames = Map(
    "newUserId" -> """["Immersion", "newUserId"]""",
     "store" ->  """["Immersion", "store"]""",
    "playCount" ->  """["Immersion", "playCount"]""",
    "retrieveSong" ->  """["Immersion", "retrieveSong"]""",
    "retrieveMetadata" ->  """["Immersion", "retrieveMetadata"]""",
    "remunerate" ->  """["Immersion", "remunerate"]""",
    "play" ->  """["Immersion", "play"]"""
  )
  val userNames = (1 to 5).map(_ => java.util.UUID.randomUUID.toString)

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

    val results = UserService(proxy).newUser(userNames.head)
    log.info(s"useradd completed with result: ${results}")
    results.isLeft === false
  }
  import coop.rchain.models.rholang.implicits._
  import coop.rchain.rholang.interpreter._
  import coop.rchain.domain.ParDecoder._

  def dataAtName:MatchResult[Boolean] = {
    val testName = contractNames("newUserId")
    log.info(s"""attempting the listenAtName: $testName)}""")
    val results = for {
      grpcRes <- proxy.dataAtNameWithTerm(testName)
      _ = log.info(s"grpcResults of name-retrivals = ${grpcRes}")
    } yield grpcRes.asString
    log.info(s"grpcResults Name  as string ${results}")
    (results.isRight && results.toOption.get.headOption.isDefined) === true
  }
}

