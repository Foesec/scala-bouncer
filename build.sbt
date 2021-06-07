name := "scala-bouncer"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Dependencies.all

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
