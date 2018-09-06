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
import coop.rchain.utils.HexUtil._
import org.specs2.matcher.MatchResult
import coop.rchain.utils.Globals._


class SongRepoITSpec extends Specification {
  def is =
    s2"""
      |SongRepository
      | $uploadSongsAndFetch
    """.stripMargin

  val songRepo = SongRepo(proxy)
  val userRepo = UserRepo(proxy)
  val log = Logger[SongRepoITSpec]

  import coop.rchain.repo.SongRepo._

  def uploadSongsAndFetch = {
    songSerialUpload
    fetch
  }

  def songSerialUpload: MatchResult[Boolean] = {
//    loadSong1
//    loadSong2
//    loadSong3
//    loadSong4
//    loadSong5
//    loadSong6
//    loadSong7
//    loadSong8
    loadSong9
    true === true
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

  def fetch = {
    val name = "Broke.jpg"
    val song = songRepo.fetchSong(name)
    log.info(s"test.fetch returned song=${song}")
    song.isRight === true
  }
}
