package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.casper.protocol.ListeningNameDataResponse
import coop.rchain.domain.{DeployAndProposeResponse, Err}
import coop.rchain.models.{Channel, Expr, Par, ParSet}
import coop.rchain.models.Channel.ChannelInstance.Quote
import coop.rchain.models.Expr.ExprInstance.GString
import org.specs2._
import coop.rchain.utils.Globals._
import coop.rchain.service.RholangContractProxy
import coop.rchain.rholang.interpreter._
import java.io.StringReader

import coop.rchain.rholang.syntax.rholang

class RholangProxySpec extends Specification { def is =s2"""
   Rnode Specification

     create new contract form file $addUser
     aliceContract $ok
     show data at contract names $dataAtName
    """

  /**
    *
  deploy and propose a new orig contract  $e0
     deploy and propose a new user $e1
  deploy usercontract as a string $e2
    *
    */
  val log=Logger[RholangProxySpec]
  val host = appCfg.getString("grpc.host")
  val grpc = RholangProxy("localhost", 40401)
  val contractProxy = RholangContractProxy(grpc)


  def aliceContract  = {
    val computed:Either[Err, DeployAndProposeResponse] = contractProxy.deployAndPropose("/rho/alice.rho")
    log.info(s"Deployed & proposed block: ${computed}")
    computed.isRight === true
  }

  def createContract  = {
    val computed:Either[Err, DeployAndProposeResponse] = contractProxy.deployAndPropose("/rho/immersion.rho")
    log.info(s"Deployed & proposed block: ${computed}")
    computed.isRight === true
  }
    def addUser = {
//      ( 1 to 5).foreach{ x =>
//        val computed: Either[Err, DeployAndProposeResponse] =
//          contractProxy.deployAndPropose("/rho/new_user.rho")
//        log.info(s"(x) - Deployed & proposed block: ${computed}")
//      }

      val computed: Either[Err, DeployAndProposeResponse] =
        contractProxy.deployAndPropose("/rho/new_user.rho")
      computed.isRight === true
    }


  import coop.rchain.models.ParSet._
  import coop.rchain.models.rholang.implicits._
  import coop.rchain.models.serialization.implicits._
  import coop.rchain.domain.ParDecoder._
  import coop.rchain.rholang.interpreter.PrettyPrinter._

  def dataAtName = {
    val names = List(
    """["Immersion", "newUserId"]""",
    """["Immersion", "playCount"]"""
    )

    val computed = grpc.dataAtName(grpc.asPar(names(0)).toOption.get)

    val userostBlocks= computed.blockResults.map(_.postBlockData)
    val expressions = userostBlocks.map(x =>  {
    val e: Par = ParSet(x).exprs(0)
     val xx = e.asDePar()
//      log.info(s"-------xx :${xx}")

    val s = PrettyPrinter().buildString(e)
//        log.info( s"pretty printing $s}")
          s
      })

//    log.info(s"userExpr :${expressions}")
     1 === 0

  }


}

