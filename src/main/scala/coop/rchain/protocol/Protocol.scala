package coop.rchain.protocol

import com.google.protobuf.ByteString
import coop.rchain.domain.{PlayCount, SongMetadata}
import cats.Monoid
import com.typesafe.scalalogging.Logger
import coop.rchain.models.rholang.implicits._
import coop.rchain.models.Expr.ExprInstance
import coop.rchain.models._
import coop.rchain.rholang.interpreter.PrettyPrinter
import coop.rchain.casper.protocol.ListeningNameDataResponse

object Protocol {
  sealed trait ValueObject
  case class SongRequest(
      songId: String,
      userId: String
  ) extends ValueObject

  case class SongResponse(
      songMetadata: SongMetadata,
      playCount: PlayCount
  ) extends ValueObject

  case class DeParConverter(asInt: List[Int] = List(),
                            asString: List[String] = List(),
                            asUri: List[String] = List(),
                            asByteArray: List[ByteString] = List())

  implicit val DeParMonoid = new Monoid[DeParConverter] {
    def empty: DeParConverter = DeParConverter()
    def combine(d1: DeParConverter, d2: DeParConverter): DeParConverter =
      DeParConverter(asInt = d1.asInt ::: d2.asInt,
                     asString = d1.asString ::: d2.asString,
                     asUri = d1.asUri ::: d2.asUri,
                     asByteArray = d1.asByteArray ::: d2.asByteArray)
  }

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
              .map(x => x.asDePar())
              .foldLeft(DeParConverter())((a, c) =>
                Monoid[DeParConverter].combine(c, a))
          case "List" =>
            log.info(s"its set. recursive call")
            e.getEListBody.ps
              .map(x => x.asDePar())
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

    def asDePar(): DeParConverter = {
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
  implicit class BlockToString(l: ListeningNameDataResponse) {
    def asString = {
      for {
        p <- l.blockResults
        b <- p.postBlockData
        s = PrettyPrinter().buildString(b)
      } yield s
    }
  }
}
