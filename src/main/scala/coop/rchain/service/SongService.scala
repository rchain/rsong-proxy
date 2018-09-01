package coop.rchain.service

import coop.rchain.domain._
import coop.rchain.repo._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import coop.rchain.protocol.Protocol._

/** service layer.
  * Des provides Repo services to api layer
  */
object SongService {

  def apply(repo: SongRepo): SongService = new SongService(repo)
}

import coop.rchain.service.moc.MocSongMetadata._
class SongService(repo: SongRepo) {

  def allSongs(userId: String, cursor: Cursor): List[SongMetadata] = {
    songMetadata(userId)
  }

  def aSong(request: SongRequest) = {
    for {
      sm <- songMetadata(request.songId).find(x => x.song.id == request.songId)
      pc = PlayCount(current = 50)
      r = SongResponse(songMetadata = sm, playCount = pc)
    } yield r
  }
  /**
  def mySongs(cursor: Cursor) =
    moc.MocSongMetadata.songMetadataList(cursor).asJson

  def mySong(req: SongRequest) =
    SongResponse(
      songMetadata = moc.MocSongMetadata.songMetadata(req.songId),
      playCount = PlayCount(current = 99)
    ).asJson

  def saveSong(song: Song, metadata: SongMetadata) = ???

//  def getSong(name: String) = repo.load(name)
}
  **/
}
