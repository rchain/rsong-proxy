package coop.rchain.repo
import coop.rchain.node.model.repl.{CmdRequest, EvalRequest, ReplGrpc}
import io.grpc._

object Rnode {
  def apply(host: String, port: Int): Rnode =
    new Rnode(host, port)
}

class Rnode(host: String, port: Int) {
  private lazy val channel =
    ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build

  def evalRholang(program: String) =
    ReplGrpc.blockingStub(channel).eval(EvalRequest(program)).output
  def runRholang(program: String) =
    ReplGrpc.blockingStub(channel).run(CmdRequest(program)).output


}

