package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain.AudioTypes
import coop.rchain.protocol.RSongModel.RSongAsset
import coop.rchain.service.moc.RSongData
import org.specs2._
import coop.rchain.utils.HexBytesUtil._

class SongRepoSpec extends Specification {
  def is =
    s2"""
       SongRepository Specifications
          base16 song conversion $ok//load to speed up
          upload song to RChain $toRNodeTest
  """
//  val proxy = RholangProxy("35.237.70.229", 40401)
  val proxy = RholangProxy("localhost", 40401)
  val log = Logger[SongRepoSpec]
  val songfile = "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
//  vall songfile = "/home/kayvan/Downloads/labels-long.ps"
  val songRepo = SongRepo()

  def load = {
    val song = songRepo.loadSongFile(songfile)
    log.info(s"loaded size = ${song.size}")
    hex2bytes(bytes2hex(song)) === song
  }

  import coop.rchain.service.moc.RSongData._

  def toRNodeTest = {
    val repository = SongRepo(proxy)
    val song = songRepo.loadSongFile(songfile)

    log.info(s"loaded size = ${song.size}")
    val songData = bytes2hex(song)

    val rsongAsset = RSongAsset(
      rsong=RSongData.rsong,
      audioType = AudioTypes.t("Stereo"),
      audioData = song,
      uri="rho://cool-song101"
    )
    val fromRnode = repository.toRnode(rsongAsset)
    log.info(s" responve from jamming songs to rnode: ${fromRnode}")
    fromRnode.isRight === true

  }
}
