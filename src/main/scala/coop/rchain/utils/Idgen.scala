package coop.rchain.utils

import javax.xml.bind.annotation.adapters.HexBinaryAdapter

object IdGen {

  private final val max_length = 40

  private final val makeId = java.security.MessageDigest.getInstance("SHA-1")
  val idGen: String => String = id =>
    (new HexBinaryAdapter)
      .marshal(makeId.digest(id.getBytes))
      .take(max_length)
      .toString
      .toLowerCase

  //TODO temp driver for idGen. Ideally should be based on a composite user-attributes
  val uuid: String = (java.util.UUID.randomUUID.toString)
}
