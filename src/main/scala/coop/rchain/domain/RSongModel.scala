package coop.rchain.domain

object RSongModel {

  sealed trait RSongModel

  case class SongBin(id: String, name: String, binData: String)
      extends RSongModel

  case class ArtWork(id: String, uri: String)

  case class RSong(
      id: String,
      isrc: String,
      iswc: String,
      cwr: String,
      upc: String,
      title: String,
      name: String,
      labelId: String,
      serviceId: String,
      featuredArtists: List[Artist],
      musician: List[String],
      language: String
  ) extends RSongModel

  //      song: Song

  case class AuthorizedTerritory(
      territory: List[String],
      temporalInterval: TemporalInterval
  ) extends RSongModel

  case class Label(
      id: String,
      name: String,
      distributorName: String,
      authorizedTerritory: AuthorizedTerritory,
      distributorId: String,
      masterRecordingCollective: Boolean
  ) extends RSongModel

  case class ConsumptionModel(
      streaming: Boolean,
      downloadable: Boolean,
      conditionalDownload: Boolean,
      rentToOwn: Boolean,
      synchronizedWithPicture: Boolean
  ) extends RSongModel

  case class RSongMetadata(
      consumptionModel: ConsumptionModel,
      label: Label,
      song: RSong,
      artWorkId: String,
      album: Album
  ) extends RSongModel

  case class RSongAsset(
      rsong: RSong,
      typeOfAsset: String, //JPG, Stereo, 3D
      assetData: String,
      metadata: RSongMetadata,
      uri: String
  )
  case class SearchModel (
    id: String,
    albumTitle: String,
    songTitle: String,
    artist: String
  )

}
