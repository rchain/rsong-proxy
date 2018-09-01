package coop.rchain.protocol

import coop.rchain.domain.{Album, Artist, Song, TemporalInterval}

object RSongModel {

  sealed trait RSongModel

  case class SongBin(id: String, name: String, binData: String)
      extends RSongModel

  case class RSong(
      id: String,
      isrc: String,
      iswc: String,
      cwr: String,
      upc: String,
      labelId: String,
      serviceId: String,
      featuredArtists: List[Artist],
      musician: List[String],
      song: Song
  ) extends RSongModel

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
      album: Album
  ) extends RSongModel

  case class RSongAsset(
      rsong: RSong,
      audioType: String,
      audioData: Array[Byte],
      uri: String
  )

}
