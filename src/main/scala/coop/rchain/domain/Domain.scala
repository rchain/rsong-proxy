package coop.rchain.domain

sealed trait Domain

case class MetaDataMapStore()
case class SongMapStore()
case class UserMapStore()
case class Entity(id: String, data: String) extends Domain
case class PlayList(entity: Entity) extends Domain

object AudioTypes {
  val t: Map[String, String] = Map("Stereo" -> "Stereo", "3D" -> "3D")
}
case class Cursor(from: Int, to: Int) extends Domain

case class Metadata(k: String, v: String) extends Domain

case class User(id: String,
                name: Option[String],
                active: Boolean,
                lastLogin: Long,
                playCount: Int = 100,
                metadata: Map[String, String])
    extends Domain

case class Artwork(id: String, uri: String) extends Domain

case class Artist(id: String, name: String) extends Domain

case class Audio(
    effect: String,
    uri: String,
    duration_ms: Long
) extends Domain

case class Song(
    id: String,
    audio: List[Audio],
    language: String
) extends Domain

case class Album(
    id: String,
    artworks: List[Artwork],
    name: String,
    duration_ms: Long,
    artists: List[Artist],
    uri: String
) extends Domain

case class SongMetadata(
    song: Song,
    artists: List[Artist],
    artwork: List[Artwork],
    album: Option[Album] = None
) extends Domain

case class PlayCount(
    current: Int // init to 100
) extends Domain

case class WorldView(
    user: User,
    songMetadata: SongMetadata,
    playCount: PlayCount
) extends Domain
