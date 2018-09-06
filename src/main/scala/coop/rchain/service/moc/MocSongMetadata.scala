package coop.rchain.service.moc
import coop.rchain.domain.RSongModel.RSongJsonAsset
import coop.rchain.domain._
import coop.rchain.repo.SongRepo
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

object MocSongMetadata {

  import coop.rchain.utils.Globals._

  val artworks = Map(
    "Broke" -> Artwork(id = "Broke",
                       uri = s"${rsongHostUrl}/${artpath}/Broke.jpg"),
    "Euphoria" -> Artwork(id = "Euphoria",
                          uri = s"${rsongHostUrl}/${artpath}/Euphoria.jpg"),
    "Tiny_Human" -> Artwork(id = "Tiny_Human",
                            uri = s"${rsongHostUrl}/${artpath}/TinyHuman.jpg")
  )
  val artists = Map(
    "Broke" -> Artist(id = "Mycle-Wastman",
                      name = "Mycle Wastman",
                      title = "Mycle Wastman"),
    "Euphoria" -> Artist(id = "California-Guitar-Trio",
                         name = "California Guitar Trio",
                         title = "California Guitar Trio"),
    "Tiny_Human" -> Artist(id = "Imogen-Heap",
                           name = "Imogen Heap",
                           title = "Imogen Heap")
  )

  val albums = Map(
    "Broke" -> Album(
      id = "Broke",
      artworks = List(artworks("Broke")),
      name = "Broke",
      title = "Broke",
      duration_ms = 10000,
      artists = List(artists("Broke")),
      uri = s"${rsongHostUrl}/${artpath}/Broke.jpg"
    ),
    "Euphoria" -> Album(
      id = "Euphoria",
      artworks = List(artworks("Euphoria")),
      name = "Euphoria",
      title = "Euphoria",
      duration_ms = 10000,
      artists = List(artists("Euphoria")),
      uri = s"${rsongHostUrl}/${artpath}/Euphoria.jpg"
    ),
    "Tiny_Human" -> Album(
      id = "album-id-Tiny_Human",
      artworks = List(artworks("Tiny_Human")),
      name = "Tiny Human",
      title = "Tiny Human",
      duration_ms = 10000,
      artists = List(artists("Tiny_Human")),
      uri = s"${rsongHostUrl}/${artpath}/TinyHuman.jpg"
    )
  )

  val song = Map(
    "Broke" ->
      Song(
        id = "Broke",
        name = "Broke",
        title = "Broke",
        audio = List(
          Audio(effect = TypeOfAsset.t("3D"),
                uri = s"${rsongHostUrl}/${songpath}/Broke_Immersive.izr",
                duration_ms = 1000L),
          Audio(effect = TypeOfAsset.t("Stereo"),
                uri = s"${rsongHostUrl}/${songpath}/Broke_Stereo.izr",
                duration_ms = 1000L)
        ),
        language = "EN"
      ),
    "Tiny_Human" ->
      Song(
        id = "Tiny_Human",
        name = "Tiny Human",
        title = "Tiny Human",
        audio = List(
          Audio(effect = TypeOfAsset.t("3D"),
                uri = s"${rsongHostUrl}/${songpath}/Tiny_Human_Immersive.izr",
                duration_ms = 1000L),
          Audio(effect = TypeOfAsset.t("Stereo"),
                uri = s"${rsongHostUrl}/${songpath}/Tiny_Human_Stereo.izr",
                duration_ms = 1000L)
        ),
        language = "EN"
      ),
    "Euphoria" ->
      Song(
        id = "Euphoria",
        name = "Euphoria",
        title = "Euphoria",
        audio = List(
          Audio(effect = TypeOfAsset.t("3D"),
                uri = s"${rsongHostUrl}/${songpath}/Euphoria_Immersive.izr",
                duration_ms = 1000L),
          Audio(effect = TypeOfAsset.t("Stereo"),
                uri = s"${rsongHostUrl}/${songpath}/Euphoria_Stereo.izr",
                duration_ms = 1000L)
        ),
        language = "EN"
      )
  )

  val mocSongs = Map(
    ("Broke" ->
      SongMetadata(song("Broke"),
                   artists = List(artists("Broke")),
                   artwork = List(artworks("Broke")),
                   album = Some(albums("Broke")))),
    ("Euphoria" ->
      SongMetadata(song("Euphoria"),
                   artists = List(artists("Euphoria")),
                   artwork = List(artworks("Euphoria")),
                   album = Some(albums("Euphoria")))),
    ("Tiny_Human" ->
      SongMetadata(song("Tiny_Human"),
                   artists = List(artists("Tiny_Human")),
                   artwork = List(artworks("Tiny_Human")),
                   album = Some(albums("Tiny_Human"))))
  )

  def songMetadata(userid: String): List[SongMetadata] = List()

  val songRepo = SongRepo(proxy)

  def loader1 = {

    val songFile =
      "/home/kayvan/dev/assets/rchain_assets/Songs/Broke_Stereo.izr"
    val brookId = "Broke_Stereo.izr"
    val typeOfAsset = "Stereo"
    val jsData = mocSongs("Broke").asJson.toString
    val assetData = songRepo.asHexConcatRsong(songFile)
    val brookAsset = RSongJsonAsset(
      id = brookId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployNoPropose(brookAsset)

  }

  def loader2 = {

    val songFile =
      "/home/kayvan/dev/assets/rchain_assets/Songs/Broke_Immersive.izr"
    val brookId = "Broke_Immersive.izr"
    val jsData = mocSongs("Broke").asJson.toString
    val assetData = songRepo.asHexConcatRsong(songFile)
    val brookAsset = RSongJsonAsset(
      id = brookId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployNoPropose(brookAsset)
  }

  def loader3 = {
    val songFile =
      "/home/kayvan/dev/assets/rchain_assets/Songs/Euphoria_Immersive.izr"
    val brookId = "Euphoria_Immersive.izr"
    val jsData = mocSongs("Euphoria").asJson.toString
    val assetData = songRepo.asHexConcatRsong(songFile)
    val brookAsset = RSongJsonAsset(
      id = brookId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployNoPropose(brookAsset)
  }

  def loader4 = {
    val songFile =
      "/home/kayvan/dev/assets/rchain_assets/Songs/Euphoria_Stereo.izr"
    val brookId = "Euphoria_Stereo.izr"
    val jsData = mocSongs("Euphoria").asJson.toString
    val assetData = songRepo.asHexConcatRsong(songFile)
    val brookAsset = RSongJsonAsset(
      id = brookId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployNoPropose(brookAsset)
  }

  def loader5 = {
    val songFile =
      "/home/kayvan/dev/assets/rchain_assets/Songs/Tiny_Human_Stereo.izr"
    val brookId = "Tiny_Human_Stereo.izr"
    val jsData = mocSongs("Tiny_Human").asJson.toString
    val assetData = songRepo.asHexConcatRsong(songFile)
    val brookAsset = RSongJsonAsset(
      id = brookId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployAndProposeAsset(brookAsset)
  }
  def loader6 = {
    val songFile =
      "/home/kayvan/dev/assets/rchain_assets/Songs/Tiny_Human_Immersive.izr"
    val brookId = "Tiny_Human_Immersive.izr"
    val jsData = mocSongs("Tiny_Human").asJson.toString
    val assetData = songRepo.asHexConcatRsong(songFile)
    val brookAsset = RSongJsonAsset(
      id = brookId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployNoPropose(brookAsset)
  }
  def loader7 = {
    val artFile =
      "/home/kayvan/dev/assets/rchain_assets/Labels/TinyHman.jpg"
    val assetId = "TinyHuman.jpg"
    val jsData = artworks("Tiny_Human").asJson.toString
    val assetData = songRepo.asHexConcatRsong(artFile)
    val theAsset = RSongJsonAsset(
      id = assetId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployNoPropose(theAsset)
  }

  def loader8 = {
    val artFile =
      "/home/kayvan/dev/assets/rchain_assets/Labels/Euphoria.jpg"
    val assetId = "Euphoria.jpg"
    val jsData = artworks("Euphoria").asJson.toString
    val assetData = songRepo.asHexConcatRsong(artFile)
    val theAsset = RSongJsonAsset(
      id = assetId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployNoPropose(theAsset)
  }

  def loader9 = {
    val artFile =
      "/home/kayvan/dev/assets/rchain_assets/Labels/Broke.jpg"
    val assetId = "Broke.jpg"
    val jsData = artworks("Broke").asJson.toString
    val assetData = songRepo.asHexConcatRsong(artFile)
    val theAsset = RSongJsonAsset(
      id = assetId,
      assetData = assetData.toOption.get,
      jsonData = jsData
    )
    songRepo.deployAndProposeAsset(theAsset)
  }

}
