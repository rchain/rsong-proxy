package coop.rchain.utils

import com.typesafe.config.ConfigFactory

object Globals {
  val cfg = ConfigFactory.load
  val appCfg = cfg.getConfig("coop.rchain.immersion")

  val artpath = "v1/art"
  val songpath = "v1/song"
  val rsongHostUrl: String = "http://dev-rchain.com"

}
