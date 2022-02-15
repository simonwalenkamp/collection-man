package errors

import play.api.libs.json.{Json, Reads, Writes}

case class ModelErrorInfo(path: String, message: String, args: Seq[String] = Seq())

object ModelErrorInfo {

  implicit val reads: Reads[ModelErrorInfo] = Json.reads[ModelErrorInfo]

  implicit val writes: Writes[ModelErrorInfo] = Json.writes[ModelErrorInfo]

}
