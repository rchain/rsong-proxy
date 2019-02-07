package coop.rchain.utils

import com.typesafe.config.{Config, ConfigFactory}
import coop.rchain.repo.RholangProxy

object Globals {
  val cfg: Config = ConfigFactory.load
  val appCfg: Config = cfg.getConfig("coop.rchain.rsong")
  val apiVersion = appCfg.getString("api.version")
  val artpath = s"$apiVersion/art"
  val songpath = s"$apiVersion/song/music"
  val rsongHostUrl: String = appCfg.getString("my.host.url")

}
