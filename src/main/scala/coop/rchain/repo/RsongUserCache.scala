package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain.{ Err, ErrorCode, PlayCount}
import coop.rchain.utils.Globals
import scalacache._
import scalacache.redis._
import scalacache.serialization.binary._
import scala.concurrent.Future
import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global
import scalacache.modes.try_._
import coop.rchain.utils.ErrImplicits._


object RSongUserCache {
  import UserRepo._

  private final val initialPlayCount=50
  val log = Logger[RSongUserCache.type]

  case class CachedUser(
    rsongUserId: String,
    playCount: PlayCount
  )
  val (redisUrl, redisPort) =
    (Globals.appCfg.getString("redis.url"),
      Globals.appCfg.getInt("redis.port"))

  implicit val rsongUserCache: Cache[CachedUser] =
    RedisCache(redisUrl, redisPort)

  def getOrCreateUser: String => RholangProxy => Either[Err,CachedUser] =
    name => proxy => {
      get(name) match {
        case Success(Some(user)) =>
          Right(user)
        case Success(None) =>
          Future {
            newUser(name)(proxy)
          }
          log.info(s"user: $name is not in cache. Creating user: $name")
          put(name)(CachedUser(name, PlayCount(initialPlayCount)) )
          Right(CachedUser(name, PlayCount(initialPlayCount)) )
        case Failure(e) =>
          Left(Err(ErrorCode.cacheLayer,e.getMessage,Some(name)))
      }
    }

  def viewPlayCount(name: String): Either[Err,PlayCount] = {
    for {
      x <- get(name).asErr
      z <- x match {
        case Some(u) => Right(u.playCount)
        case None => Left(Err(ErrorCode.unregisteredUser, "user $name is not registered", Some(name)))
      }
    } yield (z)
  }

  private def updateCache(cachedUser: CachedUser) = {
    for {
      _ <- remove(cachedUser.rsongUserId)
      v <- put(cachedUser.rsongUserId)(cachedUser)
    } yield(v)
    cachedUser
  }

  def decPlayCount(songId: String, userId: String)(proxy: RholangProxy): Either[Err,CachedUser] =
    for {
      u <- get(userId).asErr
      z <- u match {
        case Some(n) =>
          Future {
            UserRepo.decPlayCount(songId, userId)(proxy)
          }
          Right(updateCache(n.copy(playCount = PlayCount(n.playCount.current-1))))
        case None => Left(Err(ErrorCode.unregisteredUser, "Attempting to decrement playcount for unregeistered user!", Some(userId)))
      }
    } yield (z)
}
