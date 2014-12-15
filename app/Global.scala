import actors.BasketsWatcher
import akka.actor.Props
import play.api.{Play, GlobalSettings}
import play.api.libs.concurrent.Akka
import play.api.mvc.RequestHeader
import controllers.Routing
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Global extends GlobalSettings {
  override def onRouteRequest(request: RequestHeader) = Routing.onRouteRequest(request)

  override def onStart(app : play.api.Application) = {
    println(Play.current.configuration.getStringSeq("mail.baskets").getOrElse(Seq()))

    implicit val application = app
    val sys = Akka.system
    val bw = sys.actorOf(Props[BasketsWatcher], name = "basketsWatcher")
    sys.scheduler.schedule(30 minutes, 1 day)(bw ! BasketsWatcher.DeleteOld)
  }
}
