import play.api.GlobalSettings
import play.api.mvc.RequestHeader
import controllers.Routing

object Global extends GlobalSettings {

  override def onRouteRequest(request: RequestHeader) = Routing.onRouteRequest(request)

}
