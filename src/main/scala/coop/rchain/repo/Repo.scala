package coop.rchain.repo

import coop.rchain.models.Par
import coop.rchain.rholang.interpreter.PrettyPrinter
import coop.rchain.domain._

object Repo {

  implicit class ParsToString(pars: Seq[Par]) {
    def stringify() = {
      val e = pars.map(p => PrettyPrinter().buildString(p))
      if (e.isEmpty)
        Left(Err(ErrorCode.nameNotFound, s"Rholang name not found${}", None))
      else Right(e.head)
    }
  }

  def findByName(proxy: RholangProxy, rName: String): Either[Err, String] = {
    println(s"findByName recieved : $rName")
    for {
      d <- getDataAtName(proxy, s""""${rName}"""")
      _ = println(s"getDataAtname returned: ${d}")
      e <- stringify(d)
    } yield e
  }
  def getDataAtName(proxy: RholangProxy, term: String): Either[Err, Seq[Par]] =
    for {
      z <- proxy.dataAtName(term)
      pars = z.blockResults.flatMap(_.postBlockData)
    } yield pars

  def stringify: Seq[Par] => Either[Err, String] =
    pars => {
      val e = pars.map(p => PrettyPrinter().buildString(p))
      if (e.isEmpty)
        Left(Err(ErrorCode.nameNotFound, s"Rholang name not found${}", None))
      else Right(e.head)
    }
}
