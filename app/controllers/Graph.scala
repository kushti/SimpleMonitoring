package controllers

import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import org.chepurnoy.timeseries.{TimeSeriesData, TimeSeriesDoubleDatum}

object Graph extends Controller with SettingsMongo{

  def get(basket:String,number:String) = Action.async{

    def formPage(fData:Future[TimeSeriesData],number:Integer)= {
      fData.map{ data =>
        val dataNumeric = data.data.filter(_.isInstanceOf[TimeSeriesDoubleDatum])
        Ok(views.html.graph(TimeSeriesData(data.basket,dataNumeric),number))
      }
    }

    lazy val numberPointsDefault = 5

    try {
      if(number!="0") formPage(db.get(basket,number.toInt),number.toInt)
        else formPage(db.get(basket,number.toInt),numberPointsDefault)
    }
    catch {
      case t:Throwable => {
        val fData = db.get(basket,numberPointsDefault)
        formPage(fData,numberPointsDefault)
      }
    }
  }

  def get(basket:String):Action[AnyContent] = get(basket,"0")
}
