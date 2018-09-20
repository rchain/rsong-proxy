package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain.{CachingException, Err, ErrorCode}
import coop.rchain.utils.Globals
import scalacache._
import scalacache.redis._
import scalacache.serialization.binary._
import scalacache.memoization._
import scalacache.modes.try_._

import scala.util
import scala.util.{Either, Failure, Left, Right, Success, Try}

object ErrImplicits {
  implicit class _Either_[T](t: Try[T]) {
    def asErr: Either[Err, T] = {
      t match {
        case Success(s) => Right(s)
        case Failure(e) =>
          Left(Err(ErrorCode.cacheLayer, e.getMessage, None))
      }
    }
  }
  implicit class _Try_[E,T](e: Either[E,T]) {
    def asTry: Try[T] = {
      e match {
        case Right(s) => Success(s)
        case Left(f) =>
          util.Failure(CachingException(f.toString))
      }
    }
  }
}
object RSongCache {
  import ErrImplicits._

  case class CachedAsset(
    name: String,
    binaryData: Array[Byte]
  )
  val log = Logger("RSongCache")
  val repo = new SongRepo(Globals.proxy)

  val (redisUrl, redisPort)=
    (Globals.appCfg.getString("redis.url"),
      Globals.appCfg.getInt("redis.port"))

  implicit val rsongCache: Cache[CachedAsset] =
    RedisCache(redisUrl, redisPort)

  rsongCache.config
val getMemoizedAsset: String => SongRepo => Either[Err, CachedAsset] =
  name => repo => {

  def  __getMemoizedAsset(name: String): Try[CachedAsset] =
    memoize[Try, CachedAsset](None) {
      loadCache(name)(repo) match {
        case Right(s) => s
        case Left(e) =>
          log.error(s"Exception in RSongCache layer. ${e}")
          throw CachingException(e.msg)
      }
    }

    log.info(s"in memoized, attempting to fetch $name")
    __getMemoizedAsset(name).asErr
  }

  val loadCache: String =>
    SongRepo =>  Either[Err,CachedAsset]= name => repo =>
    repo.getBinaryData(name).map(CachedAsset(name, _))

}
