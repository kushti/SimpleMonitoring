name := "tsweb"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.2"

resolvers += "teamon.eu repo" at "http://repo.teamon.eu"

resolvers +=  "Local Repository" at "file:///" + baseDirectory.value + "/repository"

libraryDependencies ++=  Seq(
    "eu.teamon" %% "play-navigator" % "0.5.0",
    "timeseries" %% "timeseries" % "0.1.8" exclude("org.scala-stm", "scala-stm_2.10.0")
)


play.Project.playScalaSettings
