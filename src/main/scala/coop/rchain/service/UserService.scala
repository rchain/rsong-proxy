package coop.rchain.service

import coop.rchain.model._

object UserService {
  def find(userId: String): User =
    User(
     id= userId,
      name="immersion-user",
      active=true,
      lastLogin = System.currentTimeMillis(),
      Map(
        "key-1" -> "value-1",
        "key-2" -> "value-2",
        "key-3" -> "value-3",
        "key-4" -> "value-4")
      )
}
