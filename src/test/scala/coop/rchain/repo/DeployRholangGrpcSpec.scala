//package coop.rchain.repo
//
//import org.specs2._
//import coop.rchain.utils.Globals._
//
//class DeployRholangGrpcSpec extends Specification { def is =s2"""
//    Rnode Specification
//      Evaluate Rholang terms $e1
//      Deploy Rholang contract $e2
//      show deployed contract blocks $e3
//    """
//
//  val host = appCfg.getString("grpc.host")
//
//  def e1 = {
//    val grpcEval = EvalRholangGrpc("localhost", 40404)
//    val computed = grpcEval.evalRholang("new x in { x!(1 + 1) }")
//    println(s"----grpc response = ${computed}")
//    computed must not beEmpty
//  }
//
//  def e2 = {
//    val grpcDeploy = DeployRholangGrpc("localhost", 40401)
//    val computed= grpcDeploy.deployContract("new x in { x!(1 + 1) }")
//    computed.isRight === true
//  }
//
//  def e3 = {
//    val grpcDeploy = DeployRholangGrpc("localhost", 40401)
//    val computed= grpcDeploy.showBlock
//
//    println(s"---------------------")
//    println(s"show-blocks = ${computed}")
//    println(s"---------------------")
//    computed.toOption.isDefined === true
//  }
//
//}

