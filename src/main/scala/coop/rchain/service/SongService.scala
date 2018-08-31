package coop.rchain.service

import coop.rchain.model._
import coop.rchain.model.Protocol._
import coop.rchain.repo.SongRepo._

/** service layer.
  * Des provides Repo services to api layer
  */
object SongService {
  def mySongs(userId: String, cursor: Cursor): List[SongMetadata] = {
    songMetadata(userId)
  }
  def mySongs(songId: String)={
    for {
      sm <- songMetadata(songId).find( x => x.song.id == songId)
      pc = PlayCount(current = 50)
       r =SongResponse(songMetadata=sm, playCount=pc)
    } yield r
  }
}
