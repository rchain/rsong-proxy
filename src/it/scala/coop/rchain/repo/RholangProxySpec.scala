package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.models.Expr.ExprInstance.GString
import coop.rchain.models._
import org.specs2._
import coop.rchain.utils.Globals._
import coop.rchain.service.RholangContractProxy
import coop.rchain.domain.ParDecoder._
import coop.rchain.models.Channel.ChannelInstance.Quote

class RholangProxySpec extends Specification {
  def is =
    s2"""
   Rnode Specification

     create new contract form file $ok //createContract
     add user $ok //addUser
     show data at contract names $ok //dataAtName
    """

  val log = Logger[RholangProxySpec]
  val host = appCfg.getString("grpc.host")
  val grpc = RholangProxy("localhost", 40401)
  val contractProxy = RholangContractProxy(grpc)
  val contract = "/rho/moc.rho" 
  val userContract = "/rho/new_user.rho"


  def createContract = {
    val result = contractProxy.deployAndPropose(contract)
    log.info(s"contract creation completed with result: ${result}")
    result.isRight === true
  }

  def addUser ={
    val result = contractProxy.deployAndPropose(userContract)
    log.info(s"useradd completed with result: ${result}")
    result.isRight === true
  }

  import coop.rchain.models.rholang.implicits._
  import coop.rchain.rholang.interpreter._

  def dataAtName = {
    val names = List(
    """["Immersion", "newUserId"]""",
    """["Immersion", "playCount"]"""
    )
    val par: Par = GString(names.head)
    val ch = Channel(Quote(par))

    val asG = GString(names.head)
    val _computed = grpc.dataAtNameWithTerm(names(0))
    log.info(s"_computed = ${_computed}")
    val _computedCont = grpc.dataAtContWithTerm(names(0))
    log.info(s"_computedCONT--->  = ${_computedCont}")

    val computed = _computed.toOption.get
    val userostBlocks= computed.blockResults.map(_.postBlockData)
    val expressions = userostBlocks.map(x => x.map(e => (e.asDePar(), PrettyPrinter().buildString(e))))
      log.info(s"-------xx :${expressions}")
     1 === 0

  }


}

