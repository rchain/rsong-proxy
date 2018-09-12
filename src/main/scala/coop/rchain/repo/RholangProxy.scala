package coop.rchain.repo

import coop.rchain.casper.protocol._
import coop.rchain.domain.{Err, ErrorCode}
import com.google.protobuf.empty._
import coop.rchain.models.{Channel, Par}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import coop.rchain.domain._
import coop.rchain.rholang.interpreter._
import com.typesafe.scalalogging.Logger
import scala.util._

object RholangProxy {

  val MAXGRPCSIZE = 1024 * 1024 * 50000

  def apply(host: String, port: Int): RholangProxy = {

    val channel =
      ManagedChannelBuilder
        .forAddress(host, port)
        .maxInboundMessageSize(MAXGRPCSIZE)
        .usePlaintext(true)
        .build
    new RholangProxy(channel)
  }

}

class RholangProxy(channel: ManagedChannel) {

  private lazy val grpc = DeployServiceGrpc.blockingStub(channel)
  private lazy val log = Logger[RholangProxy]

  def shutdown = channel.shutdownNow()

  def deploy(contract: String): Either[Err, String] = {
    val resp = grpc.doDeploy(
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

  def deployNoPropose(
      contract: String): Either[Err, DeployAndProposeResponse] = {
    log.info("Deploying...")
    for {
      d <- deploy(contract)
    } yield DeployAndProposeResponse(d, "")
  }

  def deployAndPropose(
      contract: String): Either[Err, DeployAndProposeResponse] = {
    log.info("Deploying...")
    for {
      d <- deploy(contract)
      _ = log.info(s"Proposing contract $contract")
      p <- proposeBlock
    } yield DeployAndProposeResponse(d, p)
  }

  private def proposeBlock: Either[Err, String] = {
    val response: DeployServiceResponse = grpc.createBlock(Empty())
    if (response.success) {
      Right(response.message)
    } else {
      Left(Err(ErrorCode.grpcPropose, response.message, None))
    }
  }

  import coop.rchain.protocol.ParOps._
  def dataAtName(
      rholangName: String): Either[Err, ListeningNameDataResponse] = {
    log.info(s"dataAtName received name $rholangName")
    rholangName.asPar.flatMap(p => dataAtName(p))
  }

  import coop.rchain.protocol.ParOps._
  private def dataAtName(par: Par): Either[Err, ListeningNameDataResponse] = {
    log.info(s"dataAtName received par ${PrettyPrinter().buildString(par)}")
    val res = grpc.listenForDataAtName(par.asChannel)
    log.info(s"listenForDataAtName returned: $res")
    res.status match {
      case "Success" => Right(res)
      case _ =>
        log.info(s"${res}")
        Left(Err(ErrorCode.nameNotFound, s"${res}", None))
    }
  }

}
