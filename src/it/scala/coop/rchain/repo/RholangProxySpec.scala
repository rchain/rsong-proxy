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

class RholangProxySpec extends Specification { def is =s2"""
   Rnode Specification

     create new contract form file $ok
     add new user and get playCount for the user $addUser
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


  def createContract  = {
    val computed:Either[Err, DeployAndProposeResponse] = contractProxy.deployAndPropose("/rho/immersion.rho")
    log.info(s"Deployed & proposed block: ${computed}")
    computed.isRight === true
  }
    def addUser = {
      val computed:Either[Err, DeployAndProposeResponse] = contractProxy.deployAndPropose("/rho/new_user.rho")
      log.info(s"Deployed & proposed block: ${computed}")
      computed.isRight === true
    }


  def dataAtName = {
    val names = List(
    """["Immersion", "newUserId"]""",
    """["Immersion", "playCount"]"""
    )
    val computed = names map (x => grpc.dataAtName(grpc.asPar(x).toOption.get) )
    val userPars: Seq[Par]= computed.head.blockResults.flatMap( x=>x.postBlockData)
    val userPars2: Seq[Par]= computed.head.blockResults.flatMap( x=>x.postBlockData)
    import coop.rchain.models.ParSet._
    import coop.rchain.models.rholang.implicits._

    val parset: ParSet = ParSet(userPars.toBuffer,false)
    val parse2: ParSet = ParSet(userPars.toBuffer,true)
    val result = userPars2.map(x => x.toProtoString)
    val exp: Expr =  parset
    val str = parset.getGString
    log.info(s"playCountj ???? :${result}")
    log.info(s"playCount Expr  :${exp}")

    computed.head.status ==="Success"
    computed.tail.head.status ==="Success"
  }


}

