package coop.rchain.utils

import com.typesafe.config.ConfigFactory
import coop.rchain.repo.RholangProxy

object Globals {
  val cfg = ConfigFactory.load
  val appCfg = cfg.getConfig("coop.rchain.immersion")

  val artpath = "v1/art"
  val songpath = "v1/song/music"
  val rsongHostUrl: String = "http://dev-rchain.com"

  val proxy =
    RholangProxy("34.222.16.129", appCfg.getInt("grpc.ports.external"))
}
