package coop.rchain.model

sealed trait Domain

case class User (id: String, name: Option[String]) extends Domain
case class Song (id: String, name: Option[String]) extends Domain
case class play (id: String, name: Option[String], user: Option[User]) extends Domain
