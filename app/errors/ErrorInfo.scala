package errors

import _root_.util._
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json._
import play.api.mvc.Result
import play.api.mvc.Results._

import scala.reflect.ClassTag

sealed abstract class ErrorInfo(val message: String, val cause: Option[Throwable] = None) {

  def name: String = getClass.getSimpleName

  def httpStatus: Status

  def toJson: JsValue = Json.toJson(this)

  def toJsonResult: Result = httpStatus(toJson)
}

object ErrorInfo {

  private val InvalidDataFormatName = simpleNameOf[InvalidDataFormat]
  private val InvalidDataModelName  = simpleNameOf[InvalidDataModel]
  private val UnexpectedErrorName   = simpleNameOf[UnexpectedError]

  implicit val reads: Reads[ErrorInfo] = (json: JsValue) => {
    (json \ "error")
      .validate[String]
      .fold(
        errors => JsError(errors),
        errorName => {
          val reads = errorName match {
            case InvalidDataFormatName => Json.reads[InvalidDataFormat]
            case InvalidDataModelName  => invalidDataModelReads
            case UnexpectedErrorName   => unexpectedErrorReads
            case other                 => throw new RuntimeException(s"Invalid error name: $other")
          }
          reads.reads(json)
        },
      )
  }

  implicit val readsThrowable: Reads[Throwable] = (_: JsValue) => {
    JsSuccess(new Throwable)
  }

  implicit val writesThrowable: Writes[Throwable] = (_: Throwable) => {
    JsNull
  }

  val invalidDataModelReads: Reads[InvalidDataModel] = (
    (JsPath \ "schema").read[String] and
      (JsPath \ "causes").read[Seq[ModelErrorInfo]]
  )(InvalidDataModel.fromJson(_: String, _: Seq[ModelErrorInfo]))

  val unexpectedErrorReads: Reads[UnexpectedError] = (
    (JsPath \ "exception").readNullable[ExceptionInfo] and
      (JsPath \ "cause").readNullable[Throwable]
  )(UnexpectedError.fromJson(_: Option[ExceptionInfo], _: Option[Throwable]))

  implicit val writes: Writes[ErrorInfo] = (error: ErrorInfo) => {
    val commonFields = Seq("error" -> Json.toJson(error.name), "message" -> Json.toJson(error.message))

    val extraFields = (error match {
      case e: InvalidDataFormat => Json.writes[InvalidDataFormat].writes(e)
      case e: InvalidDataModel  => Json.writes[InvalidDataModel].writes(e)
      case e: UnexpectedError   => Json.writes[UnexpectedError].writes(e)
      case e: GenericError      => Json.writes[GenericError].writes(e)
    }).fields

    JsObject(commonFields ++ extraFields)
  }

}

case class InvalidDataFormat(reason: String) extends ErrorInfo("Invalid data format, expected application/json.") {
  override def httpStatus: Status = BadRequest
}

object InvalidDataFormat {
  def fromJson(reason: String): InvalidDataFormat =
    InvalidDataFormat(reason)
}

case class InvalidDataModel(schema: String, causes: Seq[ModelErrorInfo])
    extends ErrorInfo(s"Invalid data model, see schema: $schema") {
  override def httpStatus: Status = BadRequest
}

object InvalidDataModel {

  def of[T: ClassTag](validationErrors: Seq[(JsPath, Seq[JsonValidationError])]): InvalidDataModel = {
    val causes = validationErrors map { case (path, errors) =>
      ModelErrorInfo(path.toJsonString.replaceFirst("obj\\.", ""), errors.head.message)
    }

    InvalidDataModel(simpleNameOf[T], causes)
  }

  def fromJson(schema: String, causes: Seq[ModelErrorInfo]): InvalidDataModel =
    InvalidDataModel(schema, causes)

}

case class UnexpectedError(exception: Option[ExceptionInfo], override val cause: Option[Throwable])
    extends ErrorInfo("An unexpected error occurred.", cause) {
  override def httpStatus: Status = InternalServerError
}

object UnexpectedError {

  def of(t: Throwable): UnexpectedError = {

    val exceptionInfo = ExceptionInfo.of(t)

    UnexpectedError(exception = Some(exceptionInfo), cause = Some(t))
  }

  def fromJson(exception: Option[ExceptionInfo], cause: Option[Throwable]): UnexpectedError =
    UnexpectedError(exception, cause)
}

case class GenericError(statusCode: Int, override val message: String) extends ErrorInfo(message) {
  override def httpStatus: Status = Status(statusCode)
}
