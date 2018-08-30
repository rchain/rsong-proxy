package coop.rchain.service

import coop.rchain.repo.RholangProxy
import org.specs2._

class RholangContractProxySpec extends Specification {
  def is = s2"""
   Rholang Contract Specification
      classpath contains rholang contract $e1
  """
  def e1 = {
    RholangProxy("localhost", 40401)
      .immersionConstract("/rho/immersion.rho")
      .isRight === true
  }

}
