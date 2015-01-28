package controllers

import actors.{MailSettings, BasketsWatcher}
import play.api.mvc._
import play.api.libs.json._
import Json._
import org.joda.time.DateTime
import org.chepurnoy.timeseries._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.libs.concurrent.Akka
import play.api.Play
import scala.concurrent.duration._

object RestActions extends Controller with SettingsMongo with MailSettings {
  implicit val timeout = akka.util.Timeout(2 minutes)

  def post = Action(BodyParsers.parse.json) { request =>
    val json = request.body
    try {
      val basket = (json \ "basket").asOpt[String].get
      val time = new DateTime((json \ "timestamp").asOpt[Long].get)

      val tsDatum: TimeSeriesDatum = json \ "value" match {
        case n: JsNumber => TimeSeriesDoubleDatum(n.value.toDouble, time)
        case s: JsString => TimeSeriesStringDatum(s.value, time)
      }
      db.put(basket, tsDatum)

      val sendMailSignalOpt = if (urgentBaskets.contains(basket)) {
        Some(BasketsWatcher.SendMail(basket, tsDatum, urgent = true))
      } else if (mailBaskets.contains(basket)) {
        Some(BasketsWatcher.SendMail(basket, tsDatum, urgent = false))
      } else {
        None
      }

      sendMailSignalOpt map { sendMailSignal =>
        Akka.system(Play.current).actorSelection("user/basketsWatcher").resolveOne().map { ref =>
          ref ! sendMailSignal
        }
      }
      Ok(toJson("ok"))
    } catch {
      case t: Throwable => BadRequest(s"Exception during process json: $t")
    }
  }

  def get = Action.async(BodyParsers.parse.json) { request =>
    val json = request.body
    try {
      val basket = (json \ "basket").asOpt[String].get
      val count = (json \ "count").asOpt[Int].get
      val fData = db.get(basket, count)
      fData.map { data =>
        val jsonResult = toJson(Map(
          "Basket" -> toJson(data.basket),
          "TimeSeriesData" -> toJson(
            data.data.map { ts => Map(
              "Value" -> (ts match {
                case d: TimeSeriesDoubleDatum => toJson(d.value)
                case s: TimeSeriesStringDatum => toJson(s.value)
              }),
              "Time" -> toJson(ts.timestamp.getMillis / 1000)
            )}
          )
        ))
        Ok(jsonResult)
      }
    } catch {
      case t: Throwable => Future(BadRequest(s"Exception during process json: $t"))
    }
  }
}
