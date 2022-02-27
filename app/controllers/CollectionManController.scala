package controllers

import models.{ConversionData, PostmanCollection}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._

import javax.inject._

@Singleton
class CollectionManController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController
    with I18nSupport {

  def getConversionView: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val form = ConversionData.conversionForm().fill(ConversionData(""))
    Ok(views.html.convertionView(form))
  }

  def convertJson: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    ConversionData.conversionForm().bindFromRequest().fold(
      formWithErrors => {
        BadRequest(s"Invalid form! Errors: ${formWithErrors.toString}")
      },
      conversionData => {
        try {
          val json: JsValue = Json.parse(conversionData.body)
          PostmanCollection.reads.reads(json) match {
            case JsSuccess(value, _) => Ok(views.html.resultView(value.toYamlString))
            case JsError(errors) => BadRequest(s"Invalid Postman collection Json! errors: ${errors.toString()}")
          }
        } catch {
            case e: Exception => BadRequest(s"Could not parse Json! Error: ${e.getMessage}")
        }
      }
    )
  }
}
