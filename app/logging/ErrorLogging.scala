package logging

import errors.ErrorInfo
import play.api.mvc.RequestHeader
import play.api.{Logger, UsefulException}

object ErrorLogging {
  protected val logger: Logger = Logger(getClass)
  def warning(errorInfo: ErrorInfo)(implicit request: RequestHeader): Unit = {
    errorInfo.cause match {
      case Some(exception) =>
        logger.error(s"Error caught: ${errorInfo.message}", exception)
      case None =>
        logger.warn(s"Error caught: ${errorInfo.message}")
    }
  }

  def error(exception: UsefulException)(implicit request: RequestHeader): Unit = {
    logger.error(s"Exception caught: ${exception.description}", exception.cause)
  }
}
