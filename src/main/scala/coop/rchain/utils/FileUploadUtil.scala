//package coop.rchain.utils
//
//import java.time.{ZoneId, ZonedDateTime}
//
//import coop.rchain.domain._
//import coop.rchain.domain.RSongModel._
//import coop.rchain.repo.{RholangProxy, SongRepo}
//import coop.rchain.service.moc.RSongData.Brooke
//import coop.rchain.utils.Globals.{appCfg, artpath, rsongHostUrl}
//
//object FileUploadUtil {
//
//  lazy val (host, port) =
//    (appCfg.getString("grpc.host"), appCfg.getInt("grpc.ports.external"))
//  val proxy = RholangProxy(host, port)
//
//  val songRepo = SongRepo(proxy)
//
//  object Brooke {
//    val stereofile = ""
//    val d3file = ""
//    val jpgfile = ""
//
//    val artists = List(
//      Artist(id = "Mycle-Wastman",
//             title = "Mycle Wastman",
//             name = "Mycle Wastman"))
//    val artworks =
//      List(Artwork(id = "Broke", uri = s"${rsongHostUrl}/${artpath}/Brook"))
//
//  }
//  val rsong = RSong(
//    id = "Brook",
//    isrc = "Brook",
//    iswc = "Brook",
//    cwr = "Brook",
//    upc = "Brook",
//    title = "Brook",
//    name = "Brook",
//    labelId = "unkown",
//    serviceId = "unknow",
//    featuredArtists = List(
//      Artist(id = "Mycle-Wastman",
//             title = "Mycle Wastman",
//             name = "Mycle Wastman")),
//    musician = List("Broke"),
//    language = "En"
//  )
//  val zonedDateTime = ZonedDateTime.now
//  val utcZoneId = ZoneId.of("UTC")
//
//  val authorizedTerritory = AuthorizedTerritory(
//    territory = List("*"),
//    temporalInterval = TemporalInterval(
//      inMillis = Interval[Long](from = System.currentTimeMillis, to = None),
//      inUtc = Interval[String](
//        from = zonedDateTime.withZoneSameInstant(utcZoneId).toString,
//        to = None)
//    )
//  )
//  val label = Label(
//    id = "unknown",
//    name = "unknown name",
//    distributorName = "unknown",
//    authorizedTerritory = authorizedTerritory,
//    distributorId = "unknown",
//    masterRecordingCollective = true
//  )
//
//  val consumptionModel = ConsumptionModel(
//    streaming = true,
//    downloadable = false,
//    conditionalDownload = true,
//    rentToOwn = true,
//    synchronizedWithPicture = true
//  )
//
//  val album = Album(
//    id = "Broke",
//    title = "Broke",
//    name = "Broke",
//    artworks = Brooke.artworks,
//    duration_ms = 1000000,
//    artists = Brooke.artists,
//    uri = s"${rsongHostUrl}/album"
//  )
//
//  val mocMetaData =
//    RSongMetadata(consumptionModel = consumptionModel,
//                  label = label,
//                  song = rsong,
//                  artWorkId = "",
//                  album = album)
//
//  val asciidata = songRepo.asHexConcatRsong()
//  val rsongAsset = RSongAsset(
//    rsong = rsong,
//    typeOfAsset = TypeOfAsset.t("jpg"),
//    assetData = asciidata.toOption.get,
//    metadata = mocMetaData,
//    uri = "rho://cool-song101"
//  )
////  val path: String = "/www/rsong"
////  case class IMAsset(
////      id: String,
////      name: String,
////      isrc: String,
////      songFileName: String,
////      effect: String,
////      jpgFileName: String
////  )
////
////  val brook3d = IMAsset(
////    id = "Broke_Immersive",
////    name = "Broke",
////    isrc = "Broke",
////    songFileName = s"Broke_Immersive.izr",
////    effect = "3D",
////    jpgFileName = "Broke.jpg"
////  )
////  val brookStereo = IMAsset(
////    id = "Broke_Stereo",
////    name = "Broke",
////    isrc = "Broke",
////    songFileName = s"Broke_Stereo.izr",
////    effect = "Stereo",
////    jpgFileName = "Broke.jpg"
////  )
////
////  val euphoria3d = IMAsset(
////    id = "Euphoria_Immersive",
////    name = "Euphoria",
////    isrc = "Euphoria",
////    songFileName = s"Euphoria_Immersive.izr",
////    effect = "3D",
////    jpgFileName = "Euphoria.jpg"
////  )
////  val euphoriaStereo = IMAsset(
////    id = "Euphoria_Stereo",
////    name = "Euphoria",
////    isrc = "Euphoria",
////    songFileName = "Euphoria_Stereo.izr",
////    effect = "Stereo",
////    jpgFileName = "Euphoria.jpg"
////  )
////
////  val tinyHuman3d = IMAsset(
////    id = "Tiny_Human_Immersive",
////    name = "Tiny Human",
////    isrc = "Tiny_Human",
////    songFileName = s"Tiny_Human_Immersive.izr",
////    effect = "3D",
////    jpgFileName = "Tiny_Human.jpg"
////  )
////  val tinyHumanStereo = IMAsset(
////    id = "Tiny_Human_Stereo",
////    name = "Tiny Human",
////    isrc = "Tiny_Human",
////    songFileName = "Tiny_Human_Stereo.izr",
////    effect = "Stereo",
////    jpgFileName = "Tiny_Human.jpg"
////  )
////
////  val imAssetList = List(euphoria3d,
////                         euphoriaStereo,
////                         tinyHuman3d,
////                         tinyHumanStereo,
////                         euphoriaStereo,
////                         euphoria3d,
////                         brook3d,
////                         brookStereo)
//
//}
