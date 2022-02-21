name := """collection-man"""
organization := "dk.walenkamp"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
  "com.adrianhurt" %% "play-bootstrap" % "1.6.1-P28-B4"
)