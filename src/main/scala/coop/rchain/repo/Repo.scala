package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.models.Par
import coop.rchain.rholang.interpreter.PrettyPrinter
import coop.rchain.domain._

object Repo {

  val log = Logger("Repo")

  implicit class ParsToString(pars: Seq[Par]) {
    def stringify() = {
      val e = pars.map(p => PrettyPrinter().buildString(p))
      if (e.isEmpty)
        Left(Err(ErrorCode.nameNotFound, s"Rholang name not found${}", None))
      else Right(e.head)
    }
  }

  def findByName(proxy: RholangProxy, name: String): Either[Err, String] = {
    for {
      data <- getDataAtName(proxy, s""""$name"""")
      _ = log.info(s"getDataAtName returned: $data")
      dataAsString <- stringify(data)
    } yield dataAsString
  }

  def getDataAtName(proxy: RholangProxy,
                    rholangName: String): Either[Err, Seq[Par]] = {
    log.info(s"In getDataAtName. rholangName is $rholangName")
    for {
      blockInfoWithData <- proxy.dataAtName(rholangName)
      pars = blockInfoWithData.blockResults.flatMap(_.postBlockData)
    } yield pars
  }

  private def stringify: Seq[Par] => Either[Err, String] =
    pars => {
      val e: Seq[String] = pars.map(p => PrettyPrinter().buildString(p))
      if (e.isEmpty)
        Left(Err(ErrorCode.nameNotFound, s"Rholang name not found", None))
      else
        Right(e.head)
    }
}
