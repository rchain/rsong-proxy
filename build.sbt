
lazy val root = (project in file("."))
  .settings(
    organization := "coop.rchain",
    name := "immersion-rc-proxy",
    scalaVersion := "2.12.6",
    libraryDependencies ++= {
      object V {
        val http4s = "0.18.14"
        val specs2 = "4.2.0"
        val logback = "1.2.3"
        val scalalogging = "3.9.0"
        val config = "1.3.3"
      }
      Seq(
      "org.http4s"    %% "http4s-blaze-server"        % V.http4s,
      "org.http4s"    %% "http4s-circe"               % V.http4s,
      "org.http4s"    %% "http4s-dsl"                 % V.http4s,
      "org.specs2"    %% "specs2-core"                % V.specs2 % "test",
      "com.typesafe"  %  "config"                     % V.config,
      "com.typesafe.scala-logging" %% "scala-logging" % V.scalalogging, 
      "ch.qos.logback" % "logback-classic"     % V.logback
    )})
/**
scalacOptions := Seq(
  "-deprecation",
  "-unchecked",
  "-explaintypes",
  "-encoding", "UTF-8",
  "-feature",
  "-Xlog-reflective-calls",
  "-Ywarn-unused",
  "-Ywarn-value-discard",
  "-Xlint",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Xfuture",
  "-language:postfixOps",
  "-language:implicitConversions"
)
 * */

enablePlugins(UniversalPlugin)

enablePlugins(DockerPlugin)

