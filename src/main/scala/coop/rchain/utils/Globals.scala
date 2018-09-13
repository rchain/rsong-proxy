package coop.rchain.utils

import com.typesafe.config.{Config, ConfigFactory}
import coop.rchain.repo.RholangProxy

object Globals {
  val cfg: Config = ConfigFactory.load
  val appCfg: Config = cfg.getConfig("coop.rchain.immersion")

  val artpath = "v1/art"
  val songpath = "v1/song/music"
  val rsongHostUrl: String = "http://35.236.43.99"

  val proxy = RholangProxy(appCfg.getString("grpc.host"),
                           appCfg.getInt("grpc.ports.external"))
}
