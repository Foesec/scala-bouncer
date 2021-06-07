package org.flxkbr.bouncer.auth.basic

import org.flxkbr.bouncer.auth.{AuthIO, Permission}
import zio.{IO, Task}

case class BooleanEffectPermission(
    check: () => Boolean,
    message: String = "Authorization failed. Check not passed"
) extends Permission {
  override def isAuthorized: AuthIO =
    for {
      res <- IO.effect(check()).mapError(EvaluationFailure)
      _ <-
        if (res) IO.unit
        else IO.fail(BasicAuthorizationFailure(message))
    } yield ()
}

case class BooleanTotalPermission(
    check: () => Boolean,
    message: String = "Authorization failed. Check not passed"
) extends Permission {
  override def isAuthorized: AuthIO =
    for {
      res <- IO.effectTotal(check())
      _ <-
        if (res) IO.unit
        else IO.fail(BasicAuthorizationFailure(message))
    } yield ()
}

case class BooleanTaskPermission(
    check: Task[Boolean],
    message: String = "Authorization failed. Check not passed"
) extends Permission {
  override def isAuthorized: AuthIO =
    for {
      res <- check.mapError(EvaluationFailure)
      _ <-
        if (res) IO.unit
        else IO.fail(BasicAuthorizationFailure(message))
    } yield ()
}
