package coop.rchain.repo

import coop.rchain.utils.Globals
import org.specs2.Specification

class RholangProxySpec extends  Specification {
  def is = s2"""
      repo specs
          fetch by name $e1
    """
  def e0 = {
    val computed = Repo.proxy.showBlocks

    println(s"==== blocks are = ${computed}")
    computed.isEmpty === false
  }

  def e1 = {
    val immersiveName = "Broke_Immersive.izr"
    val name="Broke.jpg"
    val computed = SongRepo.getRSongAsset(name)
    println(s"==== broke.jpg = ${computed}")
    computed.isRight === true
    computed.right.get.length > 100
  }
}
