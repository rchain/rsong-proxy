//package coop.rchain.service
//
//import coop.rchain.repo.DeployRholangGrpc
//import org.specs2._
//
//class RholangContractProxySpec extends Specification  { def is =s2"""
//   Rholang Contract Specification
//      Read contract from classpath $e1
//      Deploy contract to Node $e2
//
//  """
//  def e1 = {
//    RholangContractProxy().immersionConstract("/rho/immersion.rho").isRight === true
//  }
//
//  def e2 = {
//    val computed = RholangContractProxy(DeployRholangGrpc("localhost", 40401))
//      .deploy("/rho/immersion.rho")
////    println(s"from deploying the immersion contract: \n ${computed}")
//    computed.isRight === true
//  }
//
//}
