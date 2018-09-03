package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain.AudioTypes
import coop.rchain.domain.RSongModel._
import coop.rchain.service.moc.RSongData
import org.specs2._
import coop.rchain.utils.HexBytesUtil._

class SongRepoITSpec extends Specification {
  def is =
    s2"""
       SongRepository Specifications
          base16 song conversion $ok//load to speed up
          upload song to RChain $ok//toRNodeTest
          fetching song from block $fetchSongTest
  """
//  val proxy = RholangProxy("35.237.70.229", 40401)
  val proxy = RholangProxy("localhost", 40401)
  val log = Logger[SongRepoITSpec]
  val songfile = "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
//  vall songfile = "/home/kayvan/Downloads/labels-long.ps"
  val songRepo = SongRepo()


  import coop.rchain.service.moc.RSongData._
  import coop.rchain.repo.SongRepo._

  def toRNodeTest = {
    val repository = SongRepo(proxy)
    val song = loadSongFile(songfile)
    log.info(s"loaded size = ${song.size}")
    val __songData = bytes2hex(song)
    val songData = logDepth(__songData)
    val rsongAsset = RSongAsset(
      rsong=RSongData.rsong,
      audioType = AudioTypes.t("Stereo"),
      audioData = songData,
      uri="rho://cool-song101"
    )
    val fromRnode = repository.deployAndPropose(rsongAsset)
    log.info(s" responve from jamming songs to rnode: ${fromRnode}")
    fromRnode.isRight === true

}
  def fetchSongTest = {
    val repository = SongRepo(proxy)
    val userRepo = UserRepo(proxy)
    val name = "edae0f8b-0f63-451d-8662-d0e020f49e6c-Stereo"

    val songdataName = for {
      sid <- userRepo.find(name)
      _= log.info(s"SID= ${sid}")
     queryName = s"""("$sid".hexToBytes(),"$sid-out")"""
      _=log.info(s"--- queryName = ${queryName}")
      term = s"""@["Immersion", "retrieveSong"]!${queryName}"""
      m <-proxy.deployAndPropose(term)
      songasstring= userRepo.find(s"${sid}-out")
      _=log.info(s"songAsSting.size = ${songasstring}")
    } yield songasstring




     false === false
  }
}
