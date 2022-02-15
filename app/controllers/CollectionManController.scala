package controllers

import play.api.mvc._
import javax.inject._

@Singleton
class CollectionManController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def getConversionView: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.convertionView())
  }
}
