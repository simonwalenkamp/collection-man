import errors.{ErrorInfo, InvalidDataFormat, InvalidDataModel}
import play.api.libs.json.{JsValue, Reads}
import play.api.mvc.{AnyContent, Request}

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}

package object util {

  def simpleNameOf[T: ClassTag]: String = {
    classTag[T].runtimeClass.getSimpleName
  }

  implicit class FutureExtensions[L, R](val future: Future[Either[L, R]]) extends AnyVal {

    def right[T: ClassTag](f: R => T)(implicit ec: ExecutionContext): Future[Either[L, T]] = {
      assert(!classTag[T].runtimeClass.isAssignableFrom(classOf[Future[_]]), "Did you intend to use flatRight?")
      future map {
        _.map(f)
      }
    }

    def flatRight[A >: L, T](f: R => Future[Either[A, T]])(implicit ec: ExecutionContext): Future[Either[A, T]] = {
      future flatMap {
        case Right(r) => f(r)
        case Left(l) => Immediate.left(l)
      }
    }

    def left(pf: PartialFunction[L, R])(implicit ec: ExecutionContext, ct: ClassTag[R]): Future[Either[L, R]] = {
      assert(!ct.runtimeClass.isAssignableFrom(classOf[Future[_]]), "Did you intend to use flatLeft?")
      future map {
        case Left(l) if pf.isDefinedAt(l) => Right {
          pf(l)
        }
        case either: Any => either
      }
    }

    def flatLeft(pf: PartialFunction[L, Future[Either[L, R]]])(implicit ec: ExecutionContext): Future[Either[L, R]] = {
      future flatMap {
        case Left(l) if pf.isDefinedAt(l) => pf(l)
        case other: Any => Future.successful(other)
      }
    }

  }

  implicit class RequestExtensions(val request: Request[AnyContent]) extends AnyVal {

    def bodyAs[T: ClassTag](implicit reads: Reads[T]): Either[ErrorInfo, T] = {
      if (request.hasBody) {
        request.body.asJson match {
          case Some(json) => jsonAs[T](json)
          case None => Left {
            InvalidDataFormat("Content type is not application/json")
          }
        }
      } else {
        Left {
          InvalidDataFormat("Empty request body")
        }
      }
    }

  }

  private def jsonAs[T: ClassTag](json: => JsValue)(implicit reads: Reads[T]): Either[ErrorInfo, T] = {
    json.validate[T].fold(
      errors => Left {
        InvalidDataModel.of[T](errors.map { case (x, y) =>
          x -> y.toList
        }.toSeq)
      },
      payload => Right {
        payload
      }
    )
  }
}