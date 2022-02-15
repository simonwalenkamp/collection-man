package controllers.base

import errors.ErrorInfo
import logging.ErrorLogging
import play.api.libs.json.{Json, Reads}
import play.api.mvc.{AnyContent, Request, RequestHeader, Result, Results}
import util.RequestExtensions

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

trait ApiController {
  self: Results =>

  protected def withBodyAs[T: ClassTag]
  (block: T => Future[Either[ErrorInfo, Result]])
  (implicit request: Request[AnyContent], reads: Reads[T], ec: ExecutionContext): Future[Result] = {
    request.bodyAs[T] match {
      case Right(payload) => resultOf {
        block(payload)
      }
      case Left(e) => Future.successful {
        errorOf(e)
      }
    }
  }

  protected def resultOf(future: Future[Either[ErrorInfo, Result]])
                        (implicit ec: ExecutionContext, request: RequestHeader): Future[Result] = {
    future map {
      case Right(result) => result
      case Left(error) =>
        ErrorLogging.warning(error)
        errorOf(error)
    }
  }

  private def errorOf(errorInfo: ErrorInfo) = {
    errorInfo.httpStatus {
      Json.toJson(errorInfo)
    }
  }
}
