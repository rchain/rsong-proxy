package coop.rchain.service

import cats.effect.{Effect, IO}
import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import cats.implicits._
import io.circe.Json
import io.circe.syntax._
import io.circe.generic.auto._

object UserService {
  val logger = Logger[UserService.type]

  def apply(): UserService = new UserService

  class UserService {

    def find(userId: String): Option[Json] = {
      //TODO moc user-data
      if (userId != "bad_user")
        User(
          id = userId,
          name = "immersion-user".some,
          active = true,
          lastLogin = System.currentTimeMillis(),
          playCount = 100,
          Map("key-1" -> "value-1",
              "key-2" -> "value-2",
              "key-3" -> "value-3",
              "key-4" -> "value-4")
        ).asJson.some
      else None
    }

    def updatePlayCount(id: String, playCount: Int) = {
      //TODO moc-data
      // find user and reset playcount to 100

      logger.info(s"reset plcaycount for userid: $id")
    }

    def newUser(id: String): Json = {
      User(id = id,
           name = None,
           active = true,
           lastLogin = System.currentTimeMillis,
           playCount = 100,
           Map("key-1" -> "value-1", "key-2" -> "value-2")).asJson

    }
  }

}
