package coop.rchain.domain

object ErrorCode extends Enumeration {
  type ErrorCode = Value
  val grpcEval,
  grpcDeploy,
  grpcPropose,
  grpcShow,
  rholang,
  nameToPar,
  rsongHexConversion,
  rsongRetrival,
  rsongRevnetwork,
  contractFile,
  playCountConversion,
  nameNotFound,
  unregisteredUser,
  cacheLayer,
  jsonError,
  unknown = Value
}

import ErrorCode._

case class Err(code: ErrorCode, msg: String, contract: Option[String])

