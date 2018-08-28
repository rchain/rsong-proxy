package coop.rchain.domain

import com.typesafe.scalalogging.Logger
import org.specs2._
import coop.rchain.domain.ParDecoder._
import cats.Monoid

class DeParConverterSpec extends Specification {
  def is = s2"""

  Depar specifications
     monoid test $e1
  """
  val log = Logger[DeParConverterSpec]

  def e1 = {
    val deParConverter =
      DeParConverter(asInt = List(1), asString = List("this"))

    val ds: List[DeParConverter] = (1 to 5).toList.map(x =>
      DeParConverter(asInt = List(x), asString = List(s"- $x -")))

    val res = ds.foldLeft(DeParConverter())((a, c) =>
      Monoid[DeParConverter].combine(c, a))
    log.info(s"monoid accum = ${ds}")
    log.info(s"results = ${res}")
    res.asInt === List(5, 4, 3, 2, 1)
  }

}
