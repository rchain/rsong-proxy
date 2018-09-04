package coop.rchain.utils

import com.typesafe.config.ConfigFactory

object Globals {
  val cfg = ConfigFactory.load
  val appCfg = cfg.getConfig("coop.rchain.immersion")

  val artpath = "asset/art/"
  val songpath = "asset/song/"
  val rsongHostUrl: String = "http://dev-rchain.com"

}
