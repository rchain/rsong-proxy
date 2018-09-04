package coop.rchain.utils

object FileUploadUtil {

  val path: String = "/www/rsong"
  case class IMAsset(
      id: String,
      name: String,
      isrc: String,
      songFileName: String,
      effect: String,
      jpgFileName: String
  )

  val brook3d = IMAsset(
    id = "Broke_Immersive",
    name = "Broke",
    isrc = "Broke",
    songFileName = s"Broke_Immersive.izr",
    effect = "3D",
    jpgFileName = "Broke.jpg"
  )
  val brookStereo = IMAsset(
    id = "Broke_Stereo",
    name = "Broke",
    isrc = "Broke",
    songFileName = s"Broke_Stereo.izr",
    effect = "Stereo",
    jpgFileName = "Broke.jpg"
  )

  val euphoria3d = IMAsset(
    id = "Euphoria_Immersive",
    name = "Euphoria",
    isrc = "Euphoria",
    songFileName = s"Euphoria_Immersive.izr",
    effect = "3D",
    jpgFileName = "Euphoria.jpg"
  )
  val euphoriaStereo = IMAsset(
    id = "Euphoria_Stereo",
    name = "Euphoria",
    isrc = "Euphoria",
    songFileName = "Euphoria_Stereo.izr",
    effect = "Stereo",
    jpgFileName = "Euphoria.jpg"
  )

  val tinyHuman3d = IMAsset(
    id = "Tiny_Human_Immersive",
    name = "Tiny Human",
    isrc = "Tiny_Human",
    songFileName = s"Tiny_Human_Immersive.izr",
    effect = "3D",
    jpgFileName = "Tiny_Human.jpg"
  )
  val tinyHumanStereo = IMAsset(
    id = "Tiny_Human_Stereo",
    name = "Tiny Human",
    isrc = "Tiny_Human",
    songFileName = "Tiny_Human_Stereo.izr",
    effect = "Stereo",
    jpgFileName = "Tiny_Human.jpg"
  )

  val imAssetList = List(euphoria3d,
                         euphoriaStereo,
                         tinyHuman3d,
                         tinyHumanStereo,
                         euphoriaStereo,
                         euphoria3d,
                         brook3d,
                         brookStereo)

}
