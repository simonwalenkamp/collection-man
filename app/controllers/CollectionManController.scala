package controllers

import controllers.base.ApiController
import models.ConversionData
import play.api.i18n.I18nSupport
import play.api.mvc._

import javax.inject._

@Singleton
class CollectionManController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController
    with ApiController
    with I18nSupport {

  def getConversionView: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val form = ConversionData.conversionForm().fill(ConversionData(""))
    Ok(views.html.convertionView(form))
  }

  def convertJson: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    ConversionData.conversionForm().bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.resultView("IT DID NOT WORK!"))
      },
      conversionData => {
        Ok(views.html.resultView(conversionData.body))
      }
    )
  }
}
