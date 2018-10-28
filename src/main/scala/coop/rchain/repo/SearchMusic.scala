package coop.rchain.repo

import coop.rchain.domain.RSongModel.{RSongModel, SearchModel}
import coop.rchain.service.moc.MocSongMetadata
import io.circe.Json
import coop.rchain.domain._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import org.http4s.Request

object SearchMusic {

  def search(id: String) = MocSongMetadata.getMetadata(id)
  def searchJson(payload: Json) = {

   val searchModel : Either[Err, SearchModel] =
     payload.as[RSongModel] match {
      case d: DecodingFailure =>Left(
        Err(ErrorCode.jsonError, d.message, None) )
      case s: SearchModel => Right(s)
   }
    searchModel.map(x => MocSongMetadata.getMetadata(x.id))


  }

  def searchRequest[F] =
    search("Tiny Human")
}
