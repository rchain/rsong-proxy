package coop.rchain.repo

import coop.rchain.casper.protocol._
import coop.rchain.domain.{Err, ErrorCode}
import com.google.protobuf.empty._
import io.grpc.ManagedChannelBuilder

object RholangProxy {
  def apply(host: String, port: Int): RholangProxy =
    new RholangProxy(host, port)
}

class RholangProxy(host: String, port: Int) {
  private lazy val channel =
    ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
  private lazy val deployService = DeployServiceGrpc.blockingStub(channel)

  def deployContract(contract: String) = {
     val resp = deployService.doDeploy(
      DeployData()
        .withTerm(contract)
         .withTimestamp(System.currentTimeMillis())
        .withPhloLimit(0)
       .withPhloPrice(0)
         .withNonce(0)
         .withFrom("0x1")
     )

       if (resp.success)
         Right(resp.message)
       else Left(Err(ErrorCode.grpcDeploy, resp.message, Some(contract)))
  }

  def showBlocks =
    deployService.showBlocks(Empty()).toList

  def propseBlock = {
    val response:DeployServiceResponse = deployService.createBlock(Empty())
    response.success match {
      case true => Right(response.message)
      case false => Left(Err(ErrorCode.grpcPropose, response.message, None))
    }
  }

  def dataAtName(channel: String): ListeningNameDataResponse =  ???
}
