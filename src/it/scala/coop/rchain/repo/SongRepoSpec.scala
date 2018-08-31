package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import org.specs2._
import coop.rchain.utils.HexBytesUtil._

class SongRepoSpec extends Specification { def is =
  s2"""
       SongRepository Specifications
          base16 song conversion $load to speed up
          upload song to RChain $toRchain

  """
  val log=Logger[SongRepoSpec]
  val songfile = "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
  val songRepo = SongRepo()

  def load =  {
   val song = songRepo.loadSongFile(songfile)
    log.info(s"loaded size = ${song.size}")
    hex2bytes(bytes2hex(song)) === song
  }
  def toRchain = 1 ===1
}
