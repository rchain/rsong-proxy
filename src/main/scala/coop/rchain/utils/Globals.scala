package coop.rchain.utils

import com.typesafe.config.ConfigFactory

object Globals {
  val cfg = ConfigFactory.load
  val appCfg = cfg.getConfig("coop.rchain.immersion")
}
