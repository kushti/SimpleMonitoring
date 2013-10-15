package controllers

import play.api.mvc._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global


object Table extends Controller with SettingsMongo{

  def get(basket:String) = Action.async{
    val fData = db.get(basket)
    fData.map{ data =>
      Ok(views.html.table(data))
    }
  }
}