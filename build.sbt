import sbt._
import Keys._


organization := "org.chepurnoy"

name         := "SimpleMonitoring"

version      := "0.1"

scalaVersion := "2.10.4"

resolvers ++= Seq("teamon.eu repo" at "http://repo.teamon.eu",
"time series" at "http://kushti.github.io/",
"localrepo" at "file://" + file("repo").getAbsolutePath,
"typesafe" at "http://repo.typesafe.com/typesafe/releases/",
"snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
"releases"  at "http://oss.sonatype.org/content/repositories/releases")


libraryDependencies ++= Seq(
    "eu.teamon" %% "play-navigator" % "0.5.0",
    "org.chepurnoy" %% "timeseries" % "0.1.9" exclude("org.scala-stm", "scala-stm_2.10.0")
)

 
lazy val root = (project in file(".")).enablePlugins(PlayScala)
