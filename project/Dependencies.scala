import sbt._

object Dependencies {

  val zioVersion = "1.0.9"

  private val prod = Seq(
    "org.typelevel" %% "cats-core"   % "2.3.0",
    "dev.zio"       %% "zio"         % zioVersion,
    "dev.zio"       %% "zio-streams" % zioVersion
  )

  private val test = Seq(
//    "org.scalactic" %% "scalactic"    % "3.2.9",
//    "org.scalatest" %% "scalatest"    % "3.2.9",
    "dev.zio"       %% "zio-test"     % zioVersion,
    "dev.zio"       %% "zio-test-sbt" % zioVersion
  ).map(_ % "test")

  val all: Seq[ModuleID] = prod ++ test

}
