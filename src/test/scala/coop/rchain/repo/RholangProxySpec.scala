package coop.rchain.repo

import coop.rchain.utils.Globals
import org.specs2.Specification

class RholangProxySpec extends  Specification {
  def is = s2"""
      repo specs
          fetch by name $e1
    """
  def e1 = {
    val name="Broke.jpg"
    val computed = SongRepo.getRSongAsset(name)(Globals.proxy)
    println(s"==== broke.jpg = ${computed}")
    computed.isRight === true
    computed.right.get.length > 100

  }

}
