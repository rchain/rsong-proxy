package coop.rchain.service

import coop.rchain.domain._
import coop.rchain.repo._
import coop.rchain.protocol.Protocol._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

/** service layer.
  * Des provides Repo services to api layer
  */
object SongService {

  def apply(repo: SongRepo): SongService = new SongService(repo)
}

class SongService(repo: SongRepo) {

  def mySongs(cursor: Cursor) =
    repo.songMetadataList(cursor).asJson

  def mySong(req: SongRequest) =
    SongResponse(
      songMetadata = repo.songMetadata(req.songId),
      playCount = PlayCount(current = 99)
    ).asJson

  def saveSong(song: Song, metadata: SongMetadata) = ???

//  def getSong(name: String) = repo.load(name)
}
