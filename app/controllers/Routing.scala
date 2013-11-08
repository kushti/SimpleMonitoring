package controllers

import play.navigator._


// Main router
object Routing extends PlayNavigator {

  val assets = GET on "assets" / ** to { s: String => Assets.at(path="/public", s)}

  val home = GET on root to controllers.WebActions.index _

  val table = new Namespace("table"){
    val get = GET on root / * to controllers.Table.get
  }

  val graph = new Namespace("graph"){
    val post2 = GET on  root / * / *  to controllers.Graph.get
    val post1 = GET on  root / * to controllers.Graph.get
  }

  val json = new Namespace("json"){
    val post = POST on root to controllers.RestActions.post _
    val get = GET on root to controllers.RestActions.get _
  }
}
