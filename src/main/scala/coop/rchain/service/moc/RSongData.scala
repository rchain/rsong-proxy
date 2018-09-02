package coop.rchain.service.moc

import java.time.{ZoneId, ZonedDateTime}

import coop.rchain.domain.RSongModel._
import coop.rchain.domain._

object RSongData {
  val artwork =
    Artwork(id = (java.util.UUID.randomUUID.toString), uri = "http://uri")

  val artists = List[Artist](
    Artist(id = "artist-1", name = "somecoolname", title = "a cool title"))
  val album = Album(
    id = java.util.UUID.randomUUID.toString,
    artworks = List(artwork),
    title = "album1",
    name = "album1",
    duration_ms = 1000000,
    artists = artists,
    uri = "http://uri"
  )

  val rsong = RSong(
    id = (java.util.UUID.randomUUID.toString),
    isrc = (java.util.UUID.randomUUID.toString),
    iswc = (java.util.UUID.randomUUID.toString),
    cwr = (java.util.UUID.randomUUID.toString),
    upc = (java.util.UUID.randomUUID.toString),
    labelId = (java.util.UUID.randomUUID.toString),
    serviceId = (java.util.UUID.randomUUID.toString),
    featuredArtists = artists,
    musician = List("musicion-1", "musition-2"),
    song = Song(
      id = "songId",
      title = "song title",
      name = "song name",
      audio = List(
        Audio(effect = "Stereo", uri = "rchain://id123", duration_ms = 10000)),
      language = "Clingon")
  )
  val zonedDateTime = ZonedDateTime.now
  val utcZoneId = ZoneId.of("UTC")

  val authorizedTerritory = AuthorizedTerritory(
    territory = List("US", "CA"),
    temporalInterval = TemporalInterval(
      inMillis = Interval[Long](from = System.currentTimeMillis, to = None),
      inUtc = Interval[String](
        from = zonedDateTime.withZoneSameInstant(utcZoneId).toString,
        to = None)
    )
  )
  val label = Label(
    id = java.util.UUID.randomUUID.toString,
    name = "label 1",
    distributorName = "distributor 1",
    authorizedTerritory = authorizedTerritory,
    distributorId = (java.util.UUID.randomUUID.toString),
    masterRecordingCollective = true
  )

  val consumptionModel = ConsumptionModel(
    streaming = true,
    downloadable = false,
    conditionalDownload = true,
    rentToOwn = true,
    synchronizedWithPicture = true
  )
  val rsongMetaData = RSongMetadata(
    consumptionModel = consumptionModel,
    label = label,
    song = rsong,
    album = album
  )
}
