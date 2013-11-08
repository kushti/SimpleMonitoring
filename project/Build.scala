import sbt._
import Keys._


object ApplicationBuild extends Build {

  val appName         = "SimpleMonitoring"
  val appVersion      = "0.1"

  val appDependencies = Seq(
    "eu.teamon" %% "play-navigator" % "0.5.0",
    "timeseries" %% "timeseries" % "0.1.8" exclude("org.scala-stm", "scala-stm_2.10.0")
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.2",
    resolvers += "teamon.eu repo" at "http://repo.teamon.eu",
    resolvers += "time series" at "http://kushti.github.io/repository",
    resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      "releases"  at "http://oss.sonatype.org/content/repositories/releases")
  )
}
