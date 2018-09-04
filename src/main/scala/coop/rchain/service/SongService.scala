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
    mocSongs.values.toList
  }

  def aSong(request: SongRequest) = {
    //TODO uses moc data
    for {
      sm <- songMetadata(request.songId).find(x => x.song.id == request.songId)
      pc = PlayCount(current = 50)
      r = SongResponse(songMetadata = sm, playCount = pc)
    } yield r
  }
}
