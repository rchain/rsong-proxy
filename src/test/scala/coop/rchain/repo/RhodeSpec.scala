package coop.rchain.repo

import org.specs2._

class RhodeSpec extends Specification { def is =s2"""
    Rnode Specification
      return valid response $e1
    """
  val grpcServer = Rnode("localhost", 40402)

  def e1 = {
    val reply = grpcServer.evalRholang("new x in { x!(1 + 1) }")
    println(s"----grpc response = ${reply}")
    ! reply.isEmpty
  }

}

