package org.flxkbr.bouncer
package auth.basic

import auth.AuthorizationFailure

import zio.test.Assertion._
import zio.test._

class BooleanPermissionSpec extends DefaultRunnableSpec {

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

          val program = permission.isAuthorized
          assertM(program)(equalTo(()))
        },
        testM("fail for unsuccessful check") {
          val permission = BooleanEffectPermission(failCheck, message)

          assertM(permission.isAuthorized.run)(fails(equalTo(BasicAuthorizationFailure(message))))
        }
      )
    )
}
