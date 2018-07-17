package coop.rchain.api.middleware

import cats.effect.Sync
import cats.syntax.apply._
import org.http4s.BasicCredentials
import coop.rchain.utils.Globals._

trait AuthRepository[F[_], A] {
  def persist(entity: A): F[Unit]
  def find(entity: A): F[Option[A]]
}

object AuthRepository {
  val userName = appCfg.getString("api.http.auth.user.name")
  val password = appCfg.getString("api.http.auth.user.password")
  implicit def authUserRepo[F[_]](implicit F: Sync[F]): AuthRepository[F, BasicCredentials] =
    new AuthRepository[F, BasicCredentials] {
      private val storage = scala.collection.mutable.Set[BasicCredentials](
        BasicCredentials(userName, password)
      )
      override def persist(entity: BasicCredentials): F[Unit] =
        F.delay(storage.add(entity)) *> F.unit
      override def find(entity: BasicCredentials): F[Option[BasicCredentials]] =
        F.delay(storage.find(_ == entity))
    }

}
