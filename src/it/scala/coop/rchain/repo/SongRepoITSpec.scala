package coop.rchain.repo

import java.time.{ZoneId, ZonedDateTime}

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import com.typesafe.scalalogging.Logger
import coop.rchain.domain.{Interval, TemporalInterval, TypeOfAsset}
import coop.rchain.domain.RSongModel._
import coop.rchain.service.moc.{MocSongMetadata, RSongData}
import coop.rchain.service.moc.RSongData.rsong
import org.specs2._
import coop.rchain.utils.HexBytesUtil._
import org.specs2.matcher.MatchResult


class SongRepoITSpec extends Specification {
//  def is =
//    s2"""
//       SongRepository Specifications
//          base16 song conversion $loadSong1 to speed up
//          base16 song conversion $loadSong2 to speed up
//          base16 song conversion $loadSong3 to speed up
//          base16 song conversion $loadSong4 to speed up
//          base16 song conversion $loadSong5 to speed up
//          base16 song conversion $loadSong6 to speed up
//          base16 song conversion $loadSong7 to speed up
//          base16 song conversion $loadSong8 to speed up
//          base16 song conversion $loadSong9 to speed up
//          upload song to RChain //toRNodeTest
//          upload song to RChain $ok//fakeMusicFile
//          upload song to RChain $ok//fakeMusic
//          fetching song thru fetch2 from block $ok//fetch
//          fetching song thru fetch2 from block $ok//fetch2
//          cache rsong $ok//writeSongToCacheTest
//          cache and store rsong $ok//cacheRsongTest
//  """


  def is =
    s2"""
      |SongRepository
      | song upload $songSerialUpload should work
    """.stripMargin


  //  val proxy = RholangProxy("35.237.70.229", 40401)

  val proxy = RholangProxy("localhost", 40401)
  val songRepo = SongRepo(proxy)
  val userRepo = UserRepo(proxy)
  val log = Logger[SongRepoITSpec]
  val songfile = "/home/kayvan/dev/workspaces/workspace-rchain/immersion-rc-proxy/src/test/resources/assets/Prog_Noir_iN3D.izr"
  //  vall songfile = "/home/kayvan/Downloads/labels-long.ps"


  import coop.rchain.service.moc.RSongData._
  import coop.rchain.repo.SongRepo._

  def songSerialUpload: MatchResult[Boolean] = {
//  done  loadSong1
//  done   loadSong2
// **    loadSong3
// **   loadSong4
//    loadSong5
//    loadSong6
    loadSong7
    loadSong8
// **** done   loadSong9
  }
def loadSong1 = {
  val  result = MocSongMetadata.loader1
  log.info(s"******** Brokke 3d upload results = ${result}")
  result.isRight === true

}

def loadSong2 = {
  val  result = MocSongMetadata.loader2
  log.info(s"******** Brokke 3d upload results = ${result}")
  result.isRight === true

}


def loadSong3 = {
  val  result = MocSongMetadata.loader3
  log.info(s"******** Brokke 3d upload results = ${result}")
  result.isRight === true
}


def loadSong4 = {
  val  result = MocSongMetadata.loader4
  log.info(s"******** Brokke 4d upload results = ${result}")
  result.isRight === true
}


def loadSong5 = {
  val  result = MocSongMetadata.loader5
  log.info(s"******** Brokke 5d upload results = ${result}")
  result.isRight === true
}

def loadSong6 = {
  val  result = MocSongMetadata.loader6
  log.info(s"******** Brokke 6d upload results = ${result}")
  result.isRight === true
}

def loadSong7 = {
  val  result = MocSongMetadata.loader7
  log.info(s"******** Brokke 7d upload results = ${result}")
  result.isRight === true
}

def loadSong8 = {
  val  result = MocSongMetadata.loader8
  log.info(s"******** Brokke 8d upload results = ${result}")
  result.isRight === true
}

def loadSong9 = {
  val  result = MocSongMetadata.loader9
  log.info(s"******** Brokke 9d upload results = ${result}")
  result.isRight === true
}


  def fakeMusicFile = {
    val songFile = "/www/rsong/song-1234567890XX-Stereo"
    val asciidata = songRepo.asHexConcatRsong(songFile)
    val jsonData = Json.obj("kay" -> Json.fromString("this is a string"))

    val rsonAsset=RSongJsonAsset(
      id="0123-x0xx",
      assetData = asciidata.toOption.get,
      jsonData = jsonData.toString
    )

    val fromRnode = songRepo.deployAndProposeAsset(rsonAsset)


    log.info(s" responve from jamming assets with raw json to rnode: ${fromRnode}")
    fromRnode.isRight === true
    true === true

  }


  def fakeMusic = {
    val song = hex2bytes("e04fd020ea3a6910a2d808002b30309d")
    log.info(s"loaded size = ${song.size}")
    val __songData = bytes2hex(song)
    val songData = logDepth(__songData)
    val rsongAsset = RSongAsset(
      rsong = RSongData.rsong,
      typeOfAsset = TypeOfAsset.t("Stereo"),
      assetData = songData,
      metadata = RSongData.rsongMetaData,

      uri = "rho://cool-song101"
    )
    val fromRnode = songRepo.deployAndPropose(rsongAsset)
    log.info(s" responve from jamming songs to rnode: ${fromRnode}")
    fromRnode.isRight === true

  }

  def toRNodeTest = {
    val song = loadSongFile(songfile)
    log.info(s"loaded size = ${song.size}")
    val __songData = bytes2hex(song)
    val songData = logDepth(__songData)
    val rsongAsset = RSongAsset(
      rsong = RSongData.rsong,
      typeOfAsset = TypeOfAsset.t("Stereo"),
      assetData = songData,
      metadata = RSongData.rsongMetaData,

      uri = "rho://cool-song101"
    )
    val fromRnode = songRepo.deployAndPropose(rsongAsset)
    log.info(s" responve from jamming songs to rnode: ${fromRnode}")
    fromRnode.isRight === true

  }

  def fetch = {
    val name = "song-1234567890XX-Stereo"

    val songdata = for {
      sid <- userRepo.find(name)
      _ = log.info(s"SID= ${sid}")
      queryName = s"""("$sid".hexToBytes(),"$sid-out")"""
      _ = log.info(s"--- queryName = ${queryName}")
      term = s"""@["Immersion", "retrieveSong"]!${queryName}"""
      m <- proxy.deployAndPropose(term)
      songasstring <- userRepo.find(s"${sid}-out")
      _ = log.info(s"songAsSting.size = ${songasstring.size}")
    } yield songasstring

    false === false
  }

}
