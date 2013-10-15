package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.data._
import play.api.data.Forms._
import org.chepurnoy.timeseries.{TimeSeriesData, TimeSeriesDoubleDatum}

object Graph extends Controller with SettingsMongo{

  def get(basket:String) = Action.async{
    val fData = db.get(basket)
    fData.map{ data =>
      val dataNumeric = data.data.filter(_.isInstanceOf[TimeSeriesDoubleDatum])
      Ok(views.html.graph(TimeSeriesData(data.basket,dataNumeric)))
    }
  }
}
