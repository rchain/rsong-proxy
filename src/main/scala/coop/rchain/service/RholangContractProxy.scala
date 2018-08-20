package coop.rchain.service

import coop.rchain.model.{Err, ErrorCode}
import coop.rchain.repo.DeployRholangGrpc
import coop.rchain.utils.Globals._

import scala.util.{Failure, Success, Try}

object RholangContractProxy {

  val (host, port) = (appCfg.getString("grpc.host"),
    appCfg.getInt("grpc.ports.external"))

  val grpc = DeployRholangGrpc(host, port)

  def apply(): RholangContractProxy = new RholangContractProxy(grpc)
  def apply(grpc: DeployRholangGrpc): RholangContractProxy = new RholangContractProxy(grpc)
}

class RholangContractProxy(grpc: DeployRholangGrpc) {

  val immersionConstract: String => Either[Err, String] = fileName => {
    lazy val uri = scala.io.Source.fromURI(getClass.getResource(fileName).toURI)
    Try(
      uri.getLines.reduce(_ + _ + "\n")
    ) match {
      case Success(s) =>
        uri.close
          Right(s)
      case Failure(e) =>
        uri.close
        Left(Err(ErrorCode.contractFile, fileName, None))
    }
  }
  def deploy(fileName: String): Either[Err, String] =
    for {
      r <- immersionConstract(fileName)
      g <- grpc.deployContract(r)
    } yield g

}
