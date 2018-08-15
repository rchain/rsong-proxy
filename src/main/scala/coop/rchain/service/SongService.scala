package coop.rchain.service

import coop.rchain.model._
import coop.rchain.repo.SongRepo._
import org.http4s.Uri

/** service layer.
  * Des provides Repo services to api layer
  */
object SongService {
  def mySongs(userId: String, cursor: Cursor): List[SongMetadata] = {
    songMetadata(userId)
  }
}
