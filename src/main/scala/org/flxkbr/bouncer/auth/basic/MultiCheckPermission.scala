package org.flxkbr.bouncer.auth.basic

import org.flxkbr.bouncer.auth.Permission
import zio.{IO, Task}

case class ParallelCheckPermission[E](check: IO[E, Unit], checks: IO[E, Unit]*)
    extends Permission[E] {
  override def isAuthorized: IO[E, Unit] = check.raceAll(checks)

}

case class SequentialCheckPermission(checks: Task[Boolean]*)(message: String)
    extends Permission[AuthorizationFailure] {
  override def isAuthorized: IO[AuthorizationFailure, Unit] =
    for {
      res <- checks.reduce(_.orElse(_)).mapError(EvaluationFailure)
      _   <- IO.fail(BasicAuthorizationCheckFailure(message)).unless(res)
    } yield ()
}
