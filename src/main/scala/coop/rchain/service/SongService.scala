package coop.rchain.service

import coop.rchain.model._
import coop.rchain.repo.SongRepo._

/** service layer.
  * Des provides Repo services to api layer
  */
object SongService {
  def mySongs(userId: String, cursor: Cursor): List[SongMetadata] = {
    songMetadata(userId)
  }
  def mySongs(songId: String): Option[SongMetadata] = {
    songMetadata(songId).find( x => x.song.id == songId)
  }

}
