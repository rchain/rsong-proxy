package coop.rchain.model

sealed trait Model

case class MetaDataMapStore()
case class SongMapStore()
case class UserMapStore()
case class Entity(id: String, data: String) extends Model
case class PlayList(entity: Entity)extends Model


object AudioTypes{
  val t: Map[String, String] = Map("Stereo" -> "Stereo", "3D" -> "3D")
}
case class Cursor(from: Int, to: Int) extends Model

case class Metadata(k: String, v: String) extends Model

case class User(
  id: String,
  name: String,
  active: Boolean,
  lastLogin: Long,
  metadata: Map[String, String])extends Model

case class Artwork( id: String, uri: String ) extends  Model

case class Artist( id: String, name: String ) extends  Model

case class Audio(
  effect: String,
  uri: String,
  duration_ms: Long
) extends  Model

case class Song(
  id: String,
  audio: List[Audio],
  language: String
) extends Model

case class Album(
  id: String,
  artworks: List[Artwork],
  name: String,
  duration_ms: Long,
  artists: List[Artist],
  uri: String
) extends  Model

case class SongMetadata(
  song: Song,
  artists: List[Artist],
  artwork: List[Artwork],
  album: Option[Album]=None
) extends  Model

case class PlayCount(
  max: Int,
  current: Int
) extends Model

case class UserPlayCount(
  songMetadata: SongMetadata,
  playCount: PlayCount
)

case class WorldView (
  user: User,
  songMetadata: SongMetadata,
  playCount: PlayCount
) extends  Model
