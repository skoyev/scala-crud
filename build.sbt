import sbt.Keys._

name := "car-adverts"
version := "1.0"

scalaVersion := "2.11.6"

// The Play project itself
lazy val root = (project in file(".")).enablePlugins(PlayScala)

enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator
libraryDependencies ++= Seq(

  //validator library
  "com.wix" %% "accord-core" % Dependencies.wixAccordVersion,

  //swagger
  "io.swagger" %% "swagger-play2" % Dependencies.swaggerPlayVersion,
  "org.webjars" % "swagger-ui" % Dependencies.swaggerUIVersion,

  //elastic4s
  "com.sksamuel.elastic4s" %% "elastic4s-http" % Dependencies.elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-play-json" % Dependencies.elastic4sVersion,

  //json decoder
  "org.julienrf" %% "play-json-derived-codecs" % "3.0",

  //test
  "org.scalatestplus" %% "play" % Dependencies.testPlusPlayVersion % Test,
  "com.github.javafaker"    % "javafaker"          % "0.15"  % Test
)
