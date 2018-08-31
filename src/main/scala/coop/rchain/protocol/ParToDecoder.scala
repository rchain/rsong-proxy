package coop.rchain.protocol

import java.io.StringReader

import cats.Monoid
import com.typesafe.scalalogging.Logger
import coop.rchain.casper.protocol.ListeningNameDataResponse
import coop.rchain.domain.{Err, ErrorCode}
import coop.rchain.models.Channel.ChannelInstance.Quote
import coop.rchain.models.Expr.ExprInstance
import coop.rchain.models._
import coop.rchain.models.rholang.implicits._
import coop.rchain.protocol.Protocol.DeParConverter
import coop.rchain.rholang.interpreter.{Interpreter, PrettyPrinter}

object ParOps {

  val log = Logger("DeParConverter")

  implicit class DeExpr(exp: Expr) {

    val log = Logger[DeExpr]

    def asSeqDeExp(exprs: Seq[Expr]) = {
      exprs
        .map(x => x.asDeExp())
        .foldLeft(DeParConverter())((a, c) =>
          Monoid[DeParConverter].combine(c, a))
    }

    def asDeExp(): DeParConverter = {

      def helper(e: ExprInstance): DeParConverter =
        e.typ match {
          case "Int" =>
            log.info(s"its an int: ${e.gInt}")
            DeParConverter(
              asInt =
                if (e.gInt.isDefined)
                  List(e.gInt.get)
                else List())
          case "String" =>
            log.info(s"its string: ${e.gString}")
            DeParConverter(
              asString =
                if (e.gString.isDefined)
                  List(e.gString.get)
                else List())
          case "Uri" =>
            log.info(s"its URI: ${e.gUri}")
            DeParConverter(
              asUri =
                if (e.gUri.isDefined)
                  List(e.gUri.get)
                else List())
          case "ByteArray" =>
            log.info(s"its bytearray: ${e.gUri}")
            DeParConverter(
              asByteArray =
                if (e.gByteArray.isDefined)
                  List(e.gByteArray.get)
                else List())
          case "Set" =>
            log.info(s"its set. recursive set call")
            e.getESetBody.ps
              .map(x => x.asDePar)
              .foldLeft(DeParConverter())((a, c) =>
                Monoid[DeParConverter].combine(c, a))
          case "List" =>
            log.info(s"its set. recursive call")
            e.getEListBody.ps
              .map(x => x.asDePar)
              .foldLeft(DeParConverter())((a, c) =>
                Monoid[DeParConverter].combine(c, a))
          case _ =>
            log.info(s"got nothing")
            DeParConverter()
        }

      helper(exp.exprInstance)
    }
  }

  implicit class DecodePar(par: Par) {
    def asDePar: DeParConverter = {
      par match {
        case Par(_, _, _, exprs, _, _, _, _, _, _) if (!exprs.isEmpty) =>
          exprs
            .map(x => x.asDeExp())
            .foldLeft(DeParConverter())((a, c) =>
              Monoid[DeParConverter].combine(c, a))
        case _ => DeParConverter()

      }
      par.exprs.headOption.map(x => x.asDeExp()).getOrElse(DeParConverter())
    }
  }
//  implicit class DecodeParSeq(parSeq: Seq[Par]) {
//    def asDepar(): DeParConverter = {
//      val par = parSeq.foldLeft(Par())(_ ++ _)
//      par.asDePar()
//    }
//  }

  implicit class BlockToString(l: ListeningNameDataResponse) {
    def asString = {
      for {
        p <- l.blockResults
        b <- p.postBlockData
        s = PrettyPrinter().buildString(b)
      } yield s
    }
  }
  implicit class P2C(par: Par) {
    def asChannel: Channel = Channel(Quote(par))
  }

  import coop.rchain.models.rholang.implicits._
  implicit class String2Par(rTerm: String) {
    def asPar: Either[Err, Par] = {
      val par =
        Interpreter.buildNormalizedTerm(new StringReader(rTerm)).runAttempt
      par match {
        case Left(e)  => Left(Err(ErrorCode.nameToPar, e.getMessage, None))
        case Right(r) => Right(r)
      }
    }
  }
}
