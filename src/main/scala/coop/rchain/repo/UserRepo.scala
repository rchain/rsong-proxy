package coop.rchain.repo

object UserRepo {

  val rhoAddUser =
    """@["Immersion", "newUserId"]!("userId")"""

  def apply(): UserRepo =
    new UserRepo
}
class UserRepo {

  def newUser: String = ???
  def playCount(userId: String): Int = ???
}
