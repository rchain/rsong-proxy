package coop.rchain.repo

import java.io.StringReader
import coop.rchain.casper.protocol._
import coop.rchain.domain.{Err, ErrorCode}
import com.google.protobuf.empty._
import coop.rchain.models.Channel.ChannelInstance.Quote
import coop.rchain.models.{Channel, Par}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import coop.rchain.domain._
import coop.rchain.domain.ErrorCode._
import coop.rchain.rholang.interpreter._
import com.typesafe.scalalogging.Logger
import coop.rchain.models.rholang.implicits._
import scala.util._
import coop.rchain.utils.Globals._

object RholangProxy {

  val MAXGRPCZIE = 1024 * 1024 * 5000

  def apply(host: String, port: Int): RholangProxy = {

    val channel =
      ManagedChannelBuilder
        .forAddress(host, port)
        .maxInboundMessageSize(MAXGRPCZIE)
        .usePlaintext(true)
        .build
    new RholangProxy(channel)
  }

}

class RholangProxy(channel: ManagedChannel) {

  private lazy val grpc = DeployServiceGrpc.blockingStub(channel)
  private lazy val log = Logger[RholangProxy]

  def shutdown = channel.shutdownNow()

  def deploy(contract: String) = {
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

  val deployFromFile: String => Either[Err, String] = path =>
    for {
      c <- immersionConstract(path)
      d <- deploy(c)
    } yield d

  def showBlocks =
    grpc.showBlocks(Empty()).toList

  def proposeBlock = {
    val response: DeployServiceResponse = grpc.createBlock(Empty())
    response.success match {
      case true =>
        Right(response.message)
      case false => Left(Err(ErrorCode.grpcPropose, response.message, None))
    }
  }

  def deployNoPropose(contract: String) = {
    for {
      d <- deploy(contract)
      _ = println("Proposing...")
    } yield DeployAndProposeResponse(d, "")
  }
  def deployAndPropose(contract: String) = {
    for {
      d <- deploy(contract)
      _ = println("Proposing contract = $contract...")
      p <- proposeBlock
    } yield DeployAndProposeResponse(d, p)
  }

  def dataAtContWithTerm(
      name: String): Either[Err, ListeningNameContinuationResponse] = {
    val par = Interpreter.buildNormalizedTerm(new StringReader(name)).runAttempt
    par.map(p => dataAtCont(p)) match {
      case Left(e)  => Left(Err(nameToPar, e.getMessage, None))
      case Right(r) => Right(r)
    }
  }

  import coop.rchain.protocol.ParOps._
  def dataAtName(name: String): Either[Err, ListeningNameDataResponse] = {
    log.info(s"dataAtName recived name= $name")
    name.asPar.flatMap(p => dataAtName(p))
  }

  import coop.rchain.protocol.ParOps._
  def dataAtName(par: Par): Either[Err, ListeningNameDataResponse] = {
    log.info(s"dataAtName recived par=${par}")
    val res = grpc.listenForDataAtName(par.asChannel)
    log.info(s"------ listenForDataAtName returned: $res")
    res.status match {
      case "Success" => Right(res)
      case _ =>
        println(s"----${res}")
        Left(Err(ErrorCode.nameNotFound, s"${res}", None))
    }
  }
  def dataAtCont(par: Par) = {
    val ch: Channel = Channel(Quote(par))
    grpc.listenForContinuationAtName(Channels(Seq(ch)))
  }

  val immersionConstract: String => Either[Err, String] = fileName => {
    val stream = getClass.getResourceAsStream(fileName)
    Try(
      scala.io.Source.fromInputStream(stream).getLines.reduce(_ + _ + "\n")
    ) match {
      case Success(s) =>
        stream.close
        Right(s)
      case Failure(e) =>
        stream.close
        Left(Err(ErrorCode.contractFile, fileName, None))
    }
  }

  val propose: String => Either[Err, DeployAndProposeResponse] = deployResp =>
    proposeBlock map (x =>
      DeployAndProposeResponse(fromDeploy = deployResp, fromPropose = x))
}
