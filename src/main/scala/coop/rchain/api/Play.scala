package coop.rchain.api

import cats.effect.Effect
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import coop.rchain.protos.hello._
import io.grpc._
import coop.rchain.utils.Globals._
import io.circe.generic.auto._, io.circe.syntax._


class Play[F[_]: Effect] extends Http4sDsl[F] {

  val (host, port) = (appCfg.getString("grpc.host"), appCfg.getInt("grpc.port"))
  
  val service: HttpService[F] = {
    object perPage extends OptionalQueryParamDecoderMatcher[Int] ("per_page")
    object page extends OptionalQueryParamDecoderMatcher[Int] ("page")
    object userId extends QueryParamDecoderMatcher[String] ("userId")

    HttpService[F] {
      case GET -> Root  / "song" :? userId(userId) +& perPage(pp) +& page (p) =>
        Ok(
          Json.obj(
            "song" -> Json.fromString(s" 123"),
            "user" -> Json.fromString(s" ${userId}"),
            "per_page" -> Json.fromString(s" ${pp.getOrElse(10)}"),
            "page" -> Json.fromString(s" ${p.getOrElse(0)}")))
      case GET -> Root  / id ⇒
        val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
        val request = HelloRequest(name = s"from gRpc user=${id}")
        val stub = GreeterGrpc.blockingStub(channel)
        val f: HelloReply = stub.sayHello(request)
        Ok(Json.obj("grpcmessage" -> Json.fromString(s"${f}")))
    }
  }
}
