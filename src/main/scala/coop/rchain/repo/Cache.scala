package coop.rchain.repo

import coop.rchain.utils.Globals
import scalacache._
import scalacache.redis._
import scalacache.serialization.binary._
import scalacache.memoization._
import scalacache.modes.try_._



import scala.util.Try

object RsongCache {
  case class CachedAsset(
    name: String,
    binaryData: Array[Byte]
  )
  val (redisUrl, redisPort)= (Globals.appCfg.getString("redis.url"), Globals.appCfg.getInt("redis.port"))
  implicit val rsongCache: Cache[CachedAsset] =
    RedisCache(redisUrl, redisPort)

  def getCachedAsset(name: String): Try[CachedAsset] =
    memoize[Try, CachedAsset](None) {
      val res = rsongCache.get(name)
      CachedAsset(name, new Array[Byte](10))
    }

  def loadCach(name: String) = {

  }

}
