package coop.rchain.service

import coop.rchain.model.{Artist, Artwork, Cursor, SongMetadata}

/** service layer.
  * Des provides Repo services to api layer
  */
class Song {
  def mySongs(userId: String, cursor: Cursor): List[SongMetadata] = {
   List(
     SongMetadata(
       isrc="rc-B123",
       trackUrl = "https://someurl-1",
       artists = List(Artist(id="artist-1", name="famous-artist-1")),
       artwork = List(Artwork(id="rc-artwork-1", url="https://artwork-url-1"))
     ),
     SongMetadata(
       isrc="rc-B456",
       trackUrl = "https://someurl-2",
       artists = List(Artist(id="artist-2", name="famous-artist-2")),
       artwork = List(Artwork(id="rc-artwork-2", url="https://artwork-url-2"))
     )
   )
  }

}
