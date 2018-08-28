package coop.rchain.repo

import coop.rchain.casper.protocol._
import coop.rchain.domain.{Err, ErrorCode}
import com.google.protobuf.empty._
import coop.rchain.models.Channel.ChannelInstance.Quote
import coop.rchain.models.{Channel, Par}
import coop.rchain.models.Expr.ExprInstance.GString
import io.grpc.ManagedChannelBuilder
import coop.rchain.domain._
import coop.rchain.domain.ErrorCode._
import coop.rchain.rholang.interpreter._
import java.io.StringReader
import coop.rchain.models.rholang.implicits._

object RholangProxy {

  def apply(host: String, port: Int): RholangProxy =
    new RholangProxy(host, port)
}

class RholangProxy(host: String, port: Int) {
  private lazy val channel =
    ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
  private lazy val deployService = DeployServiceGrpc.blockingStub(channel)
  def shutdown = channel.shutdownNow()

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

  def proposeBlock = {
    val response: DeployServiceResponse = deployService.createBlock(Empty())
    response.success match {
      case true  => Right(response.message)
      case false => Left(Err(ErrorCode.grpcPropose, response.message, None))
    }
  }

  def deployAndPropse(contract: String) = {
    for {
      d <- deployContract(contract)
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
  def dataAtNameWithTerm(
      name: String): Either[Err, ListeningNameDataResponse] = {
    val par = Interpreter.buildNormalizedTerm(new StringReader(name)).runAttempt
    par.map(p => dataAtName(p)) match {
      case Left(e)  => Left(Err(nameToPar, e.getMessage, None))
      case Right(r) => Right(r)
    }
  }

  def dataAtName(s: String) = {
    val par: Par = GString(s)
    val ch: Channel = Channel(Quote(par))
    val rep = deployService.listenForDataAtName(ch)
    rep
  }

  def dataAtCont(par: Par) = {
    val ch: Channel = Channel(Quote(par))
    val rep = deployService.listenForContinuationAtName(Channels(Seq(ch)))
    rep
  }
  def dataAtName(par: Par) = {
    val ch: Channel = Channel(Quote(par))
    val rep = deployService.listenForDataAtName(ch)
    rep
  }
}
