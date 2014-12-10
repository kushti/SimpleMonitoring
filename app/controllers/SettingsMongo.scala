package controllers

import play.api.Play
import collection.JavaConversions._
import mongo.MongoOperations


trait SettingsMongo {

  val dbName = Play.current.configuration.getString("db.name").getOrElse("time_series")

  val servers = Play.current.configuration.getStringList("server.names").
    map(_.toList).getOrElse(List("localhost"))

  val db = new MongoOperations(servers, dbName)

}
