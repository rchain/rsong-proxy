package coop.rchain.api.middleware

// import cats._, cats.data._, cats.implicits._
import cats.effect.Sync
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.server.middleware.authentication.BasicAuth


class AuthUser[F[_]](implicit F: Sync[F],
                     R: AuthRepository[F, BasicCredentials])
    extends Http4sDsl[F] {

  private val authedService: AuthedService[BasicCredentials, F] =
    AuthedService {
      case GET -> Root as user =>
        Ok(s"Access Granted: ${user.username}")
    }

  private val authMiddleware: AuthMiddleware[F, BasicCredentials] =
    BasicAuth[F, BasicCredentials]("Protected Realm", R.find)

  val service: HttpService[F] = authMiddleware(authedService)

}
