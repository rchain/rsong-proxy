package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import org.specs2._
import coop.rchain.utils.Globals._
import coop.rchain.service.RholangContractProxy

class DeployRholangGrpcSpec extends Specification { def is =s2"""
   Rnode Specification
      Deploy Rholang contract $e1
      propose locks $e2
      show block $e3
      deploy and poropse add user to contract $e5
    """

  val log=Logger[DeployRholangGrpcSpec]
  val host = appCfg.getString("grpc.host")
  val grpcDeploy = DeployRholangGrpc("localhost", 40401)

    def e1 = {
      val computed = RholangContractProxy(grpcDeploy)
        .deploy("/rho/tut-iterate.rho")
      log.debug(s"===========deployed contract===========")
      log.debug(s"from deploying the immersion contract: \n ${computed}")
      log.debug(s"===========End deployed contract===========")
      computed.isRight === true
    }

  def e2 = {
    val computed= grpcDeploy.propseBlock
    log.debug(s"------proposed block result ----------")
    log.debug(s" ${computed}")
    log.debug(s"------End proposed block result ----------")
    computed.toOption.isDefined === true
  }


  def e3 = {
    val computed= grpcDeploy.showBlocks
    log.debug(s"+++++++++++++++++++++ show blocks +++++++++++++++++")
    log.debug(s"show-blocks = ${computed}")
    log.debug(s"+++++++++++++++++End show blocks +++++++++++++++++")
    computed.toOption.isDefined === true
  }


  def e5 = {
    val grpcDeploy = DeployRholangGrpc("localhost", 40401)
    val contract =
      """
        |@["Immersion", "newUserId"]!("123UniqueUser543Kayvan1")
      """.stripMargin
    log.debug(s"deploying contract: ${contract}")
    val computed = grpcDeploy.deployContract(contract)

    log.debug(s"################deployed user-contract ############ ")
   computed.fold(
     e => log.error(s"$e"),
     s => log.info(s)
     )
    log.debug(s"##############End deployed user-contract ############ ")
    computed.isRight === true
   }

}

