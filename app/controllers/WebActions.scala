package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.data._
import play.api.data.Forms._


object WebActions extends Controller with SettingsMongo{

  def index = Action.async{
    val fBaskets = db.basketsList()
    fBaskets.map{baskets=>
      Ok(views.html.index(baskets))
    }
  }




}