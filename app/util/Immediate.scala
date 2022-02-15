package util

import scala.concurrent.Future

object Immediate {

  def apply[T](value: T): Future[T] = Future.successful(value)

  def left[L, R](value: L): Future[Either[L, R]] = apply(Left(value))

  def right[L, R](value: R): Future[Either[L, R]] = apply(Right(value))

}
