import CompilerSettings._
import TodoListPlugin._

  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"))

lazy val root = (project in file("."))
  .settings(
    organization := "coop.rchain",
    name := "rsong-proxy",
    scalaVersion := "2.12.6",
    libraryDependencies ++= {
      object V {
        val http4s = "0.19.0-M1"
        val specs2 = "4.2.0"
        val logback = "1.2.3"
        val scalalogging = "3.9.0"
        val config = "1.3.3"
        val scalapb= "0.7.4"
        val circie="0.9.3"
        val catsEffect="1.0.0-RC3"
        val monix="3.0.0-RC1"
        val redisCache="0.24.3"
        val kamon = "0.6.6"
      }
      Seq(
       "io.monix" %% "monix" % V.monix,
        "com.github.cb372" %% "scalacache-redis" % V.redisCache,
       "org.http4s" %% "http4s-dsl" % V.http4s,
       "org.http4s" %% "http4s-blaze-server" % V.http4s,
        "org.http4s" %% "http4s-circe" % V.http4s,
        "io.circe" %% "circe-core" % V.circie,
        "io.circe" %% "circe-generic" % V.circie,
        "io.circe" %% "circe-parser" % V.circie,
        "org.specs2" %% "specs2-core" % V.specs2 % "test",
        "com.typesafe" %  "config" % V.config,
        "com.typesafe.scala-logging" %% "scala-logging" % V.scalalogging, 
        "com.thesamet.scalapb" %% "compilerplugin" % V.scalapb,
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
        "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
        "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "ch.qos.logback" % "logback-classic" % V.logback,
        "io.kamon" %% "kamon-core" % V.kamon,
        "io.kamon" %% "kamon-statsd" % V.kamon,
        "io.kamon" %% "kamon-log-reporter" % V.kamon,
        "io.kamon" %% "kamon-system-metrics" % V.kamon,
        "org.aspectj" % "aspectjweaver" % "1.8.9"

      )})

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value)

// scalacOptions := CompilerSettings.options

enablePlugins(JavaServerAppPackaging, BuildInfoPlugin)

compileWithTodolistSettings

javaOptions in Universal += s"-Dkamon.auto-start=true"


dockerRepository := Some("kayvank")
dockerUpdateLatest := true
version in Docker := version.value + "-" + scala.sys.env.getOrElse(
  "CIRCLE_BUILD_NUM", default = "local")

