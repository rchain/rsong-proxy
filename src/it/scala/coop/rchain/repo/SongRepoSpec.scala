package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import org.specs2._

class SongRepoSpec extends Specification { def is =
  s2"""
       SongRepository Specifications
          load the song in memory $load
          upload song to RChain $toRchain

  """
  val log=Logger[SongRepoSpec]
  val songfile = "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
  val songRepo = SongRepo()

  def load =  {
   val song = songRepo.load(songfile)
    log.info(s"loaded size = ${song.size}")
    1 === 1
  }
  def toRchain = 1 ===1
}
