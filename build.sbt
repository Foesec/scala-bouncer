name := "scala-bouncer"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("org.flxkbr.bouncer")

libraryDependencies ++= Dependencies.all

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
