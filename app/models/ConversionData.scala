package models

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}

case class ConversionData(body: String)

object ConversionData {

  def apply(): ConversionData =
    ConversionData("")

  def conversionForm(): Form[ConversionData] =
    Form(
      mapping(
        "body" -> nonEmptyText
      )(ConversionData.apply)(ConversionData.unapply)
    )
}
