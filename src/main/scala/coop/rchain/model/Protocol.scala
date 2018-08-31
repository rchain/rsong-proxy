package coop.rchain.model

object Protocol {
  sealed trait ValueObject

  case class SongRequest(
    songId: String,
    userId: String) extends ValueObject
  case class SongResponse(
    songMetadata: SongMetadata,
    playCount: PlayCount
  ) extends ValueObject
}
