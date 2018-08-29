package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import org.specs2._
import coop.rchain.utils.Globals._
import coop.rchain.service.{RholangContractProxy, UserService}
import coop.rchain.domain.ParDecoder._

class RholangProxySpec extends Specification {
  def is =
    s2"""
   Rnode Specification

     create new contract form file $ok//createContract
     add user $addUser
     show data at contract names $dataAtName

    """

  val log = Logger[RholangProxySpec]
  val host = appCfg.getString("grpc.host")
  val grpc = RholangProxy("localhost", 40401)
  val contractProxy = RholangContractProxy(grpc)
  val contract = "/rho/immersion.rho"
  val userContract = "/rho/addUser.rho"


  def createContract = {
    val result = contractProxy.deployAndPropose(contract)
    log.info(s"contract creation completed with result: ${result}")
    result.isRight === true
  }

  def addUser ={
//    val result = contractProxy.deployAndPropose(userContract)
    val result = UserService(grpc).newUser()
    log.info(s"useradd completed with result: ${result}")
    result.isRight === true
  }


  import coop.rchain.models.rholang.implicits._
  import coop.rchain.rholang.interpreter._

  def dataAtName = {
    val names = List(
//    """["Immersion", "newUserId"]""",
//    """["Immersion", "playCount"]""",
    """"userId"""",
    """"countOut"""",
    """"a2632184b-0231-4eb2-9eb1-822cc600e49f""""
    )
//    val userIdCont = grpc.dataAtContWithTerm(names(0))
//    log.info(s"_computedCONT--->  = ${userIdCont}")
    (0 to 2).foreach( i => {
      val userIdData = grpc.dataAtNameWithTerm(names(i))
      log.info(s"_computed = ${userIdData}")
      val computed = userIdData.toOption.get
      val userIdBlockData = computed.blockResults.flatMap(_.postBlockData)
      val blockData = userIdBlockData.map(e => (e.asDePar()))
      val blockDataPrettyPrint = userIdBlockData.map(e => PrettyPrinter().buildString(e))
      log.info(s"--blockData :${blockData}")
      log.info(s"--PrettyPrint blockData - ${names(i)} :${blockDataPrettyPrint}")
    })
     1 === 0

  }


}

