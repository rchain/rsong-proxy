package coop.rchain.service.moc

import java.time.{ZoneId, ZonedDateTime}

import coop.rchain.domain.RSongModel._
import coop.rchain.domain._

object RSongData {
  import coop.rchain.utils.Globals._

  object Brooke {
    val artists = List(
      Artist(id = "Mycle-Wastman",
             title = "Mycle Wastman",
             name = "Mycle Wastman"))
    val artworks =
      List(Artwork(id = "Broke", uri = s"${rsongHostUrl}/${artpath}/Brook"))

  }

  val album = Album(
    id = "Broke",
    title = "Broke",
    name = "Broke",
    artworks = Brooke.artworks,
    duration_ms = 1000000,
    artists = Brooke.artists,
    uri = s"${rsongHostUrl}/album"
  )

  val rsong = RSong(
    id = "Brook",
    isrc = "Brook",
    iswc = "Brook",
    cwr = "Brook",
    upc = "Brook",
    title = "Brook",
    name = "Brook",
    labelId = "unkown",
    serviceId = "unknow",
    featuredArtists = Brooke.artists,
    musician = List("Broke"),
    language = "En"
  )

  val zonedDateTime = ZonedDateTime.now
  val utcZoneId = ZoneId.of("UTC")

  val authorizedTerritory = AuthorizedTerritory(
    territory = List("*"),
    temporalInterval = TemporalInterval(
      inMillis = Interval[Long](from = System.currentTimeMillis, to = None),
      inUtc = Interval[String](
        from = zonedDateTime.withZoneSameInstant(utcZoneId).toString,
        to = None)
    )
  )
  val label = Label(
    id = "unknown",
    name = "unknown name",
    distributorName = "unknown",
    authorizedTerritory = authorizedTerritory,
    distributorId = "unknown",
    masterRecordingCollective = true
  )

  val consumptionModel = ConsumptionModel(
    streaming = true,
    downloadable = false,
    conditionalDownload = true,
    rentToOwn = true,
    synchronizedWithPicture = true
  )
  val rsongMetaData =
    RSongMetadata(consumptionModel = consumptionModel,
                  label = label,
                  song = rsong,
                  album = album)

}
