package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import coop.rchain.utils.Globals
import scalacache._
import scalacache.redis._
import scalacache.serialization.binary._
import scalacache.memoization._
import scala.util._
import scalacache.modes.try_._
import coop.rchain.utils.ErrImplicits._


object RSongAssetCache {

  val log = Logger[RSongAssetCache.type]
  val binaryAsset: String => Either[Err, Array[Byte]] = name =>
    SongRepo.getRSongAsset(name)

  val (redisUrl, redisPort) =
    (Globals.appCfg.getString("redis.url"),
      Globals.appCfg.getInt("redis.port"))

  implicit val rsongCache: Cache[CachedAsset] =
    RedisCache(redisUrl, redisPort)

  rsongCache.config
  val getMemoizedAsset: String => Either[Err, CachedAsset] =
    name => {

      def __getMemoizedAsset(name: String): Try[CachedAsset] =
        memoize[Try, CachedAsset](None) {
          SongRepo.getRSongAsset(name).map(CachedAsset(name, _)) match {
            case Right(s) =>
              log.debug(s"Found asset. ${s}")
              s
            case Left(e) =>
              log.error(s"Exception in RSongCache layer. ${e}")
              throw CachingException(e.msg)
          }
        }

      log.info(s"in memoized, attempting to fetch $name")
      __getMemoizedAsset(name).asErr
    }
}
