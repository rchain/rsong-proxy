package coop.rchain.repo

import coop.rchain.domain._
import coop.rchain.models.Par
import coop.rchain.rholang.interpreter.PrettyPrinter
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import coop.rchain.domain._

object Repo {

  def find: RholangProxy => String => Either[Err, String] =
    grpc =>
      rName =>
        for {
          d <- dataAtNameAsPar(grpc)(s""""${rName}"""")
          e <- dataAtName(d)
        } yield e

  def dataAtNameAsPar: RholangProxy => String => Either[Err, Seq[Par]] =
    grpc =>
      term =>
        for {
          z <- grpc.dataAtName(term)
          pars = z.blockResults.flatMap(_.postBlockData)
        } yield pars

  def dataAtName: Seq[Par] => Either[Err, String] =
    pars => {
      val e = pars.map(p => PrettyPrinter().buildString(p))
      if (e.isEmpty)
        Left(Err(ErrorCode.nameNotFount, s"Rholang name not found${}", None))
      else Right(e.head)
    }
}
