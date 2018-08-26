package coop.rchain.service

import coop.rchain.domain.{Err, ErrorCode, DeployAndProposeResponse}
import coop.rchain.repo.RholangProxy
import coop.rchain.utils.Globals._

import scala.util.{Failure, Success, Try}

object RholangContractProxy {

  val (host, port) =
    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))

  val grpc = RholangProxy(host, port)

  def apply(): RholangContractProxy = new RholangContractProxy(grpc)
  def apply(grpc: RholangProxy): RholangContractProxy =
    new RholangContractProxy(grpc)
}

class RholangContractProxy(grpc: RholangProxy) {

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
  def deployAndPropose(
      fileName: String): Either[Err, DeployAndProposeResponse] =
    for {
      r <- immersionConstract(fileName)
      e <- grpc.deployAndPropse(r)
    } yield e

}
