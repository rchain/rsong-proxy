package coop.rchain.repo
import scala.concurrent.Future._
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.global
import coop.rchain.utils.Globals._
import coop.rchain.protos.hello._
import io.grpc._

object ContractService {
private class Impl extends GreeterGrpc.Greeter {
  override def sayHello(req: HelloRequest) = {
    val reply = HelloReply(message = "Hello " + req.name)
    Future.successful(reply)
  }
}
  def apply(port: Int = appCfg.getInt("grpc.port")): Server = {
    val server = ServerBuilder.forPort(port).addService(
      GreeterGrpc.bindService(new Impl,ExecutionContext.global)).build.start
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      server.shutdown
      System.err.println("*** server shut down")
    }
    server
  }
}
