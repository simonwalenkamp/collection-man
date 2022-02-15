package errors

import play.api.libs.json.{Json, Reads, Writes}
import play.libs.exception.ExceptionUtils


case class ExceptionInfo(name: String, message: String, stacktrace: Seq[String])

object ExceptionInfo {

  def of(exception: Throwable): ExceptionInfo = {
    // Get stack trace as array of lines with tabs removed.
    val stacktrace = ExceptionUtils
      .getStackFrames(exception)
      .map(_.trim)

    ExceptionInfo(
      exception.getClass.getName,
      exception.getMessage,
      stacktrace.toIndexedSeq)
  }

  implicit val reads: Reads[ExceptionInfo] = Json.reads[ExceptionInfo]

  implicit val writes: Writes[ExceptionInfo] = Json.writes[ExceptionInfo]

}