package org.flxkbr.bouncer.auth.basic

import org.flxkbr.bouncer.auth.Permission
import zio.{IO, Task}

case class BooleanEffectPermission(
    check: () => Boolean,
    message: String = "Authorization failed. Check not passed"
) extends Permission[AuthorizationFailure] {
  override def isAuthorized: IO[AuthorizationFailure, Unit] =
    for {
      res <- IO.effect(check()).mapError(EvaluationFailure)
      _   <- IO.fail(BasicAuthorizationCheckFailure(message)).unless(res)
    } yield ()
}

case class BooleanTotalPermission(
    check: () => Boolean,
    message: String = "Authorization failed. Check not passed"
) extends Permission[AuthorizationFailure] {
  override def isAuthorized: IO[AuthorizationFailure, Unit] =
    for {
      res <- IO.effectTotal(check())
      _   <- IO.fail(BasicAuthorizationCheckFailure(message)).unless(res)
    } yield ()
}

case class BooleanTaskPermission(
    check: Task[Boolean],
    message: String = "Authorization failed. Check not passed"
) extends Permission[AuthorizationFailure] {
  override def isAuthorized: IO[AuthorizationFailure, Unit] =
    for {
      res <- check.mapError(EvaluationFailure)
      _   <- IO.fail(BasicAuthorizationCheckFailure(message)).unless(res)
    } yield ()
}
