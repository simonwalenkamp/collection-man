package controllers

import controllers.base.ApiController
import models.{ConversionData, PostmanCollection}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
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
        Ok(views.html.resultView(formWithErrors.errors.toString()))
      },
      conversionData => {
        val json: JsValue = Json.parse(conversionData.body)
        val test = json.validate[PostmanCollection]
        val postmanCollection = test.get

        val result = postmanCollection.toYamlString

        Ok(views.html.resultView(result))
      }
    )
  }
}
