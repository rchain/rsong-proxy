package coop.rchain.repo

import coop.rchain.node.model.repl.{CmdRequest, EvalRequest, ReplGrpc}
import io.grpc._

object EvalRholangProxy {
  def apply(host: String, port: Int): EvalRholangProxy =
    new EvalRholangProxy(host, port)
}

class EvalRholangProxy(host: String, port: Int) {
  private lazy val channel =
    ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build

  def evalRholang(program: String) =
    ReplGrpc.blockingStub(channel).eval(EvalRequest(program)).output
  def runRholang(program: String) =
    ReplGrpc.blockingStub(channel).run(CmdRequest(program)).output
}
