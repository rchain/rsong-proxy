package coop.rchain.service

import com.typesafe.scalalogging.Logger
import coop.rchain.model._
import io.circe.parser.decode
import io.circe.generic.auto._
import io.circe.parser._
import io.circe._
import io.circe.generic.semiauto._

object UserService {
val logger = Logger[UserService.type ]
  case class UserPC(userId: String, songId: String, increment: Int)

  def find(userId: String): Option[User] ={
   //TODO moc user-data
    if(userId != "bad_user")
      Some(User(
     id= userId,
      name="immersion-user",
      active=true,
      lastLogin = System.currentTimeMillis(),
      Map(
        "key-1" -> "value-1",
        "key-2" -> "value-2",
        "key-3" -> "value-3",
        "key-4" -> "value-4")
      )) else None
    }

  def updatePlayCount(userPC: UserPC): Unit =  {
    //TODO moc-data
   logger.info(s"${userPC}")
  }


}
