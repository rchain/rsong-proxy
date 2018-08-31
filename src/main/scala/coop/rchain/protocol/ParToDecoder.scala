package coop.rchain.protocol

import cats.Monoid
import com.typesafe.scalalogging.Logger
import coop.rchain.casper.protocol.ListeningNameDataResponse
import coop.rchain.models.Expr.ExprInstance
import coop.rchain.models._
import coop.rchain.models.rholang.implicits._
import coop.rchain.protocol.Protocol.DeParConverter
import coop.rchain.rholang.interpreter.PrettyPrinter

object ParDecoder {

  val log = Logger("DeParConverter")

//  implicit class DecodePar(par: Par) {
//
//    def asDePar(): DeParConverter = {
//      log.info(s"deparing ${par}")
//      par match {
//        case Par(_, _, _, exprs, _, _, _, _, _, _) if (!exprs.isEmpty) =>
//          exprs
//            .map(x => x.asDeExp())
//            .foldLeft(DeParConverter())((a, c) =>
//              Monoid[DeParConverter].combine(c, a))
//        case _ => DeParConverter()
//
//      }
//      par.exprs.headOption.map(x => x.asDeExp()).getOrElse(DeParConverter())
//    }
//  }
//  implicit class BlockToString(l: ListeningNameDataResponse) {
//    def asString = {
//      for {
//        p <- l.blockResults
//        b <- p.postBlockData
//        s = PrettyPrinter().buildString(b)
//      } yield s
//    }
//  }
}
