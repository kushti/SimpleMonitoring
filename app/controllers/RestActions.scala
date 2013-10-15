package controllers

import play.api.mvc._
import play.api.libs.json._
import Json._
import org.joda.time.DateTime
import org.chepurnoy.timeseries._
import mongo.MongoOperations
import play.api.Play
import collection.JavaConversions._

object RestActions extends Controller with SettingsMongo{
  //todo: def get = Action{}

  def post = Action(BodyParsers.parse.json){request =>
    val json = request.body
    try {
      val basket = (json \ "basket").asOpt[String].get
      val time = new DateTime((json \ "timestamp").asOpt[Long].get)

      val tsDatum:TimeSeriesDatum = json \"value"  match {
        case n:JsNumber =>  TimeSeriesDoubleDatum(n.value.toDouble,time)
        case s:JsString => TimeSeriesStringDatum(s.value,time)
      }
      db.put(basket,tsDatum)
      val result = toJson("ok")
      Ok(result)
    }catch{
      case t: Throwable => BadRequest(s"Exception during process json: $t")
    }
  }
}
