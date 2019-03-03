import CompilerSettings._
import TodoListPlugin._

scalaVersion := "2.12.8"

  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"))

val gitCommitString = SettingKey[String]("gitCommit")

gitCommitString := git.gitHeadCommit.value.getOrElse("Not Set")

scalacOptions ++= Seq(
  "-encoding", "utf8", // Option and arguments on same line
  "-Xfatal-warnings",  // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

javacOptions ++= Seq("-encoding", "UTF-8")

lazy val root = (project in file("."))
  . settings(
    buildInfoKeys := Seq[BuildInfoKey](version, gitCommitString),
    buildInfoPackage := "buildInfo",
    buildInfoOptions += BuildInfoOption.ToMap,
    buildInfoOptions += BuildInfoOption.ToJson
  ).settings(
    organization := "coop.rchain",
    name := "rsong-proxy",
    scalaVersion := "2.12.8",
    libraryDependencies ++= {
      object V {
        val http4s = "0.19+"
        val specs2 = "4.2.0"
        val logback = "1.2.3"
        val scalalogging = "3.9.0"
        val config = "1.3.3"
        val scalapb= "0.8.2"
        val circie="0.9.3"
        val catsEffect="1.0.0"
        val monix="3.0.0-RC2-d0feeba"
        val redisCache="0.24.3"
        val kamon = "0.6.6"
        val magnolia = "0.10.0"
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
        "com.typesafe.scala-logging" %% "scala-logging" % V.scalalogging, 
        "com.thesamet.scalapb" %% "compilerplugin" % V.scalapb,
       "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
       "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
       "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "io.grpc"  % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
       "com.propensive" %% "magnolia" % V.magnolia,
       "ch.qos.logback" % "logback-classic" % V.logback,
       "io.kamon" %% "kamon-prometheus" % "1.0.0"

      )},
  PB.targets in Compile := Seq( scalapb.gen() -> (sourceManaged in Compile).value),
  buildInfoKeys in Compile := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, git.gitHeadCommit),
  buildInfoPackage := "buildInfo" )

enablePlugins(JavaServerAppPackaging, BuildInfoPlugin)

enablePlugins(GitVersioning)

compileWithTodolistSettings

dockerRepository := Some("kayvank")
dockerUpdateLatest := true
version in Docker := version.value + "-" + scala.sys.env.getOrElse(
  "CIRCLE_BUILD_NUM", default = "local")

