package coop.rchain.model

sealed trait Model

case class MetaDataMapStore()
case class SongMapStore()
case class UserMapStore()
case class Entity(id: String, data: String) extends Model
case class PlayList(entity: Entity)extends Model

case class Cursor(from: Int, to: Int) extends Model
case class Metadata(k: String, v: String) extends Model
case class User(id: String, name: String, metadata: List[Metadata])extends Model
case class Artwork( id: String, url: String ) extends  Model
case class Artist(id: String, name: String) extends  Model
case class Song(isrc: String, url: String, duration: Long, language: String) extends  Model
case class SongMetadata(
                       isrc: String,
                       trackUrl: String,
                       artists: List[Artist],
                       artwork: List[Artwork]
                       )
