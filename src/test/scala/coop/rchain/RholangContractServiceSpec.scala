package coop.rchain

import coop.rchain.repo.DeployRholangGrpc
import coop.rchain.service.RholangContractService
import org.specs2._

import scala.util.Try

class RholangContractServiceSpec extends Specification  { def is =s2"""
   Rholang Contract Specification
      Read contract from classpath $e1
      Deploy contract to Node $e2
      update Rholang contract $e3

  """
  def e1 = {
    RholangContractService().immersionConstract("/rho/immersion.rho").isRight === true
  }

  def e2 = {
    val computed = RholangContractService(DeployRholangGrpc("localhost", 40403))
      .deploy("/rho/immersion.rho")
   println(s"from deploying the immersion contract: \n ${computed}")
    computed.isRight === true
}

  def e3= 1 === 1

}
