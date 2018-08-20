package coop.rchain.repo

import coop.rchain.node.model.repl.{CmdRequest, EvalRequest, ReplGrpc}
import io.grpc._

object EvalRholangGrpc {
  def apply(host: String, port: Int): EvalRholangGrpc =
    new EvalRholangGrpc(host, port)
}

class EvalRholangGrpc(host: String, port: Int) {
  private lazy val channel =
    ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build

  def evalRholang(program: String) =
    ReplGrpc.blockingStub(channel).eval(EvalRequest(program)).output
  def runRholang(program: String) =
    ReplGrpc.blockingStub(channel).run(CmdRequest(program)).output
}
