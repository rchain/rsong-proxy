import sbt._

object CompilerSettings {
  val scalacOptions = Seq(
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
}
