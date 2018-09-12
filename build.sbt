import CompilerSettings._

lazy val projectSettings = Seq(
  organization := "coop.rchain",
  scalaVersion := "2.12.6",
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")),
  scalafmtOnCompile := true
)

lazy val compilerSettings = CompilerSettings.options ++ Seq(
  crossScalaVersions := Seq("2.11.12", scalaVersion.value))

lazy val commonSettings = projectSettings // ++ compilerSettings

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    organization := "coop.rchain",
    name := "immersion-rc-proxy",
    scalaVersion := "2.12.6",
    libraryDependencies ++= {
      object V {
        val http4s = "0.19.0-M1"
        val specs2 = "4.2.0"
        val logback = "1.2.3"
        val scalalogging = "3.9.0"
        val config = "1.3.3"
        val scalapb= "0.7.4"
        val dropbox="3.0.8"
        val circie="0.9.3"
        val catsEffect="1.0.0-RC3",
      }
      Seq(
       "io.monix" %% "monix" % "3.0.0-RC1" ,
        // "org.typelevel" %% "cats-effect" % V.catsEffect,

       "org.http4s" %% "http4s-dsl" % V.http4s,
       "org.http4s" %% "http4s-blaze-server" % V.http4s,
        "org.http4s" %% "http4s-circe" % V.http4s,
        "io.circe" %% "circe-core" % V.circie,
        "io.circe" %% "circe-generic" % V.circie,
        "io.circe" %% "circe-parser" % V.circie,
        "org.specs2" %% "specs2-core" % V.specs2 % "it, test",
        "com.typesafe" %  "config" % V.config,
        "com.typesafe.scala-logging" %% "scala-logging" % V.scalalogging, 
        "com.thesamet.scalapb" %% "compilerplugin" % V.scalapb,
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
        "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
        "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "ch.qos.logback" % "logback-classic" % V.logback
      )})
    .configs(IntegrationTest)
    .settings( Defaults.itSettings: _* )

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value)

// scalacOptions := CompilerSettings.scalacOptions

Test / parallelExecution := false
parallelExecution in IntegrationTest := false

enablePlugins(JavaServerAppPackaging)

dockerRepository := Some("kayvank")
dockerUpdateLatest := true
version in Docker := version.value + "-" + scala.sys.env.getOrElse(
  "CIRCLE_BUILD_NUM", default = "local")
