package coop.rchain.protocol

import com.google.protobuf.ByteString
import coop.rchain.domain.{PlayCount, SongMetadata}
import cats.Monoid

object Protocol {

  sealed trait ValueObject

  case class SongRequest(
      songId: String,
      userId: String
  ) extends ValueObject

  case class SongResponse(
      songMetadata: SongMetadata,
      playCount: PlayCount
  ) extends ValueObject

  case class DeParConverter(asInt: List[Int] = List(),
                            asString: List[String] = List(),
                            asUri: List[String] = List(),
                            asByteArray: List[ByteString] = List())

  implicit val DeParMonoid = new Monoid[DeParConverter] {
    def empty: DeParConverter = DeParConverter()
    def combine(d1: DeParConverter, d2: DeParConverter): DeParConverter =
      DeParConverter(asInt = d1.asInt ::: d2.asInt,
                     asString = d1.asString ::: d2.asString,
                     asUri = d1.asUri ::: d2.asUri,
                     asByteArray = d1.asByteArray ::: d2.asByteArray)
  }
}
