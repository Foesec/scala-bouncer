package org.flxkbr.bouncer.auth.basic

import zio.Task
import zio.test.Assertion._
import zio.test._

object BooleanPermissionSpec extends DefaultRunnableSpec {

  val okCheck: () => Boolean   = () => true
  val failCheck: () => Boolean = () => false

  val message = "basic message"

  val exception                          = new Exception("exception during evaluation")
  val evaluationException: () => Boolean = () => throw exception

  override def spec: Spec[Any, TestFailure[AuthorizationFailure], TestSuccess] =
    suite("BooleanPermissionSpec")(
      suite("BooleanEffectPermission")(
        testM("succeeds for successful check") {
          val permission = BooleanEffectPermission(okCheck)
          val program    = permission.isAuthorized
          assertM(program)(equalTo(()))
        },
        testM("fail for unsuccessful check") {
          val permission = BooleanEffectPermission(failCheck, message)
          assertM(permission.isAuthorized.run)(
            fails(equalTo(BasicAuthorizationCheckFailure(message)))
          )
        },
        testM("wrap exceptions during check") {
          val permission = BooleanEffectPermission(evaluationException, message)
          assertM(permission.isAuthorized.run)(fails(equalTo(EvaluationFailure(exception))))
        }
      ),
      suite("BooleanTotalPermission")(
        testM("succeeds for successful check") {
          val permission = BooleanTotalPermission(okCheck, message)
          assertM(permission.isAuthorized)(isUnit)
        },
        testM("fail for unsuccessful check") {
          val permission = BooleanTotalPermission(failCheck, message)
          assertM(permission.isAuthorized.run)(
            fails(equalTo(BasicAuthorizationCheckFailure(message)))
          )
        },
        testM("dies on exception during check") {
          val permission = BooleanTotalPermission(evaluationException, message)
          assertM(permission.isAuthorized.run)(dies(equalTo(exception)))
        }
      ),
      suite("BooleanTaskPermission")(
        testM("succeed for successful check") {
          val okTask     = Task.succeed(true)
          val permission = BooleanTaskPermission(okTask, message)
          assertM(permission.isAuthorized)(isUnit)
        },
        testM("fail for unsuccessful check") {
          val failTask   = Task.succeed(false)
          val permission = BooleanTaskPermission(failTask, message)
          assertM(permission.isAuthorized.run)(
            fails(equalTo(BasicAuthorizationCheckFailure(message)))
          )
        },
        testM("wrap exception in task") {
          val exceptionTask: Task[Boolean] = Task.fail(exception)
          val permission                   = BooleanTaskPermission(exceptionTask, message)
          assertM(permission.isAuthorized.run)(fails(equalTo(EvaluationFailure(exception))))
        }
      )
    )
}
