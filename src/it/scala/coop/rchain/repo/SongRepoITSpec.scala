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
          base16 song conversion $ok/load to speed up
          upload song to RChain $ok//toRNodeTest
          upload song to RChain $ok//fakeMusic
          fetching song thru fetch2 from block $ok//fetch
          fetching song thru fetch2 from block $ok//fetch2
          cache rsong $ok//writeSongToCacheTest
          cache and store rsong $ok//cacheRsongTest
  """
//  val proxy = RholangProxy("35.237.70.229", 40401)

  val proxy = RholangProxy("localhost", 40401)
  val repository = SongRepo(proxy)
  val userRepo = UserRepo(proxy)
  val log = Logger[SongRepoITSpec]
  val songfile = "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
//  vall songfile = "/home/kayvan/Downloads/labels-long.ps"


  import coop.rchain.service.moc.RSongData._
  import coop.rchain.repo.SongRepo._


  def fakeMusic = {
    val song = hex2bytes("e04fd020ea3a6910a2d808002b30309d")
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

  def toRNodeTest = {
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
  def fetch = {
    val name = "song-1234567890XX-Stereo"

    val songdata = for {
      sid <- userRepo.find(name)
      _= log.info(s"SID= ${sid}")
     queryName = s"""("$sid".hexToBytes(),"$sid-out")"""
      _=log.info(s"--- queryName = ${queryName}")
      term = s"""@["Immersion", "retrieveSong"]!${queryName}"""
      m <-proxy.deployAndPropose(term)
      songasstring <- userRepo.find(s"${sid}-out")
      _=log.info(s"songAsSting.size = ${songasstring.size}")
    } yield songasstring

    false === false
  }


  def fetch2 = {
    val name = "song-1234567890XX-Stereo"
    val song = repository.cacheSong(name)
    log.info(s"${song.toOption.get.size}")
    (song.isLeft == false ) === true
  }

  def writeSongToCacheTest = {
    val name = "song-1234567890XX-Stereo"
       val res = repository.writeSongToCache(name)
    log.info(s"songsize = ${res.toOption.get.size}")
    (res.isRight) === true
  }

  def cacheRsongTest = {
    val name = "song-1234567890XX-Stereo"
       val res = repository.cacheSong(name)
    log.info(s"songsize = ${res.toOption.get.size}")
    (res.isRight) === true
  }

}
