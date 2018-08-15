package coop.rchain.repo

import coop.rchain.model._

object SongRepo {
  val artworks = Map(
    "Prog_Noir" -> Artwork(
      id="artwork-id-Prog_Noir",
      uri="https://s3.amazonaws.com/dev-q2io-rchain/v1/assets/art-work/ProgNoirImage.jpeg"
    ),
      "Tiny_Human" -> Artwork(
        id="artwork-id-Tiny_Human",
        uri="https://s3.amazonaws.com/dev-q2io-rchain/v1/assets/art-work/TinyHumanImage.3.jpeg")
  )
  val artists= Map (
    "Prog_Noir" -> Artist(id="artist-id-Prog_Noir", name= "Prog_Noir" ),
    "Tiny_Human" -> Artist(id="artist-id-Tiny_Human", name= "Tiny_Human" )
  )

  val albums = Map(
    "Prog_Noir" -> Album(
      id="album-id-Prog_Noir",
      artworks=List(artworks("Prog_Noir")),
      name="Prog_Noir_1st_album",
      duration_ms = 10000,
      artists = List(artists("Prog_Noir")),
      uri="http://prog_noir-uri"
    ),
    "Tiny_Human" -> Album(
      id="album-id-Tiny_Human",
      artworks=List(artworks("Tiny_Human")),
      name="Tiny_Human_1st_album",
      duration_ms = 10000,
      artists = List(artists("Tiny_Human")),
      uri="http://Tiny-human-uri" )
  )

  val song = Map(
    "Prog_Noir" ->
      Song(id=s"song-id-1",
            audio= List(
              Audio(
                effect=AudioTypes.t("3D"),
                uri = "https://s3.amazonaws.com/dev-q2io-rchain/v1/assets/music/Prog_Noir_iN3D.izr",
                duration_ms=1000L),
              Audio(
                effect=AudioTypes.t("Stereo"),
                uri ="https://s3.amazonaws.com/dev-q2io-rchain/v1/assets/music/Prog_Noir_Stereo.izr" ,
                duration_ms=1000L) ),
            language="EN"),
    "Tiny_Human" ->
      Song(id="song-id-2",
            audio = List( 
              Audio(
                effect=AudioTypes.t("3D"),
                uri = "https://s3.amazonaws.com/dev-q2io-rchain/v1/assets/music/Tiny_Human_iN3D.izr",
                duration_ms=1000L),
              Audio(
                effect=AudioTypes.t("Stereo"),
                uri = "https://s3.amazonaws.com/dev-q2io-rchain/v1/assets/music/Tiny_Human_Stereo.izr",
                duration_ms=1000L)) ,
              language="EN") ) 

  val songMetadata: String => List[SongMetadata] = userId => 
  List(
    SongMetadata(
      song("Prog_Noir"),
      artists = List(artists("Prog_Noir")),
      artwork=List(artworks("Prog_Noir")),
        album=Some(albums("Prog_Noir")) 
    ),
    SongMetadata(
      song("Tiny_Human"),
      artists = List(artists("Tiny_Human")),
      artwork=List(artworks("Tiny_Human")),
      album=Some(albums("Tiny_Human")) )
    )
}
