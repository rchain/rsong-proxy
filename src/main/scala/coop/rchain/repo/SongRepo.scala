package coop.rchain.repo

import com.dropbox.core.v2.paper.ListUsersOnPaperDocResponse
import coop.rchain.model._

object SongRepo {
  val artworks = Map(
    "Broke" -> Artwork(
      id = "Broke",
      uri =
        "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/art-work/Broke.jpg"),
    "Euphoria" -> Artwork(
      id = "Euphoria",
      uri =
        "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/art-work/Euphoria.jpg"),
    "Tiny_Human" -> Artwork(
      id = "Tiny_Human",
      uri =
        "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/art-work/TinyHuman.jpg")
  )
  val artists = Map(
    "Broke" -> Artist(id = "Broke", name = "Broke"),
    "Euphoria" -> Artist(id = "Euphoria", name = "Euphoria"),
    "Tiny_Human" -> Artist(id = "Tiny_Human", name = "Tiny_Human")
  )

  val albums = Map(
    "Broke" -> Album(
      id = "Broke",
      artworks = List(artworks("Broke")),
      name = "Broke",
      duration_ms = 10000,
      artists = List(artists("Broke")),
      uri =
        "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/art-work/Broke.jpg"
    ),
    "Euphoria" -> Album(
      id = "Euphoria",
      artworks = List(artworks("Euphoria")),
      name = "Euphoria",
      duration_ms = 10000,
      artists = List(artists("Euphoria")),
      uri =
        "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/art-work/Euphoria.jpg"
    ),
    "Tiny_Human" -> Album(
      id = "album-id-Tiny_Human",
      artworks = List(artworks("Tiny_Human")),
      name = "Tiny_Human_1st_album",
      duration_ms = 10000,
      artists = List(artists("Tiny_Human")),
      uri =
        "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/art-work/TinyHuman.jpg"
    )
  )

  val song = Map(
    "Broke" ->
      Song(
        id = "Broke",
        audio = List(
          Audio(effect = AudioTypes.t("3D"),
            uri =
              "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/music/Broke_Immersive.izr",
            duration_ms = 1000L)),
        language = "EN"
      ),
    "Tiny_Human" ->
      Song(
        id = "Tiny_Human",
        audio = List(
          Audio(
            effect = AudioTypes.t("3D"),
            uri =
              "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/music/Tiny_Human_Immersive.izr",
            duration_ms = 1000L),
          Audio(
            effect = AudioTypes.t("Stereo"),
            uri =
              "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/music/Tiny_Human_Stereo.izr",
            duration_ms = 1000L)
        ),
        language = "EN"
      ),
    "Euphoria" ->
      Song(
        id = "Euphoria",
        audio = List(
          Audio(
            effect = AudioTypes.t("3D"),
            uri =
              "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/music/Euphoria_Immersive.izr",
            duration_ms = 1000L),
          Audio(
            effect = AudioTypes.t("Stereo"),
            uri =
              "https://s3.amazonaws.com/dev-q2io-rchain/v2/assets/music/Euphoria_Stereo.izr",
            duration_ms = 1000L)
        ),
        language = "EN"
      )
  )

  val mocSongs = List(
    SongMetadata(
      song("Broke"),
      artists = List(artists("Broke")),
      artwork = List(artworks("Broke")),
      album = Some(albums("Broke"))
    ),
    SongMetadata(
      song("Euphoria"),
      artists = List(artists("Euphoria")),
      artwork = List(artworks("Euphoria")),
      album = Some(albums("Euphoria"))
    ),
    SongMetadata(
      song("Tiny_Human"),
      artists = List(artists("Tiny_Human")),
      artwork = List(artworks("Tiny_Human")),
      album = Some(albums("Tiny_Human"))
    )
  )

  val songMetadata: String => List[SongMetadata] = userId => mocSongs

}
