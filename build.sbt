name := """collection-man"""
organization := "dk.walenkamp"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"
val playPac4jVersion = "11.1.0-PLAY2.8"
val pac4jVersion = "5.3.0"

libraryDependencies ++= Seq(
  guice,
  "org.pac4j" %% "play-pac4j" % "11.1.0-PLAY2.8",
  "org.pac4j" % "pac4j-oidc" % "5.3.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.1",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.13.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
  "com.adrianhurt" %% "play-bootstrap" % "1.6.1-P28-B4"
)