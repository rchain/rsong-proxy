package coop.rchain.domain

import java.time.{ZoneId, ZonedDateTime}

import coop.rchain.domain.RSongModel._

object RSongData {
  val artwork =
    Artwork(id = (java.util.UUID.randomUUID.toString), uri = "http://uri")

  val album = Album(
    id = java.util.UUID.randomUUID.toString,
    artworks = List(artwork),
    name = "aalbum1",
    duration_ms = 1000000,
    artists = List[Artist](Artist("1", "artist-1")),
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
    featuredArtists = List("artist-1", "artist-2"),
    musician = List("musicion-1", "musition-2"),
    title = "song title-1"
  )
  val zonedDateTime = ZonedDateTime.now
  val utcZoneId = ZoneId.of("UTC")

  val authorizedTerritory = AuthorizedTerritory(
    terriroty = List("US", "CA"),
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
