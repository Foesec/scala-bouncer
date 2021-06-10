package org.flxkbr.bouncer.auth.basic

import zio.Ref
import zio.clock._
import zio.duration.durationInt
import zio.test.Assertion._
import zio.test._
import zio.test.environment._

object MultiCheckPermissionSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[TestEnvironment, Any] =
    suite("MultiCheckPermissionSpec")(
      suite("ParallelCheckPermission")(
        testM("evaluates checks in parallel") {
          val startedTracking   = Ref.make(Seq.empty[Int])
          val completedTracking = Ref.make(Seq.empty[Int])

          val c1 = for {
            start    <- startedTracking
            _        <- start.update(_ :+ 1)
            complete <- completedTracking
            _        <- complete.update(_ :+ 1)
          } yield ()

          val c2 = for {
            start    <- startedTracking
            _        <- start.update(_ :+ 2)
            complete <- completedTracking
            _        <- complete.update(_ :+ 2)
          } yield ()

          val c3 = for {
            start    <- startedTracking
            _        <- start.update(_ :+ 3)
            complete <- completedTracking
            _        <- complete.update(_ :+ 3)
          } yield ()

          val permission = ParallelCheckPermission(c1, c2, c3)

          assertM(permission.isAuthorized)(isUnit)
        }
      )
    )
}
