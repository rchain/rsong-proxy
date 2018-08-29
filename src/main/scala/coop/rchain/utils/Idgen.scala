package coop.rchain.utils

object IdGen {

  //TODO temp driver for idGen. Ideally should be based on a composite user-attributes
  def id = (java.util.UUID.randomUUID.toString).filterNot(_ == '-')
}
