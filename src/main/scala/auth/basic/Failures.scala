package org.flxkbr.bouncer
package auth.basic

import auth.AuthorizationFailure

case class BasicAuthorizationFailure(message: String) extends AuthorizationFailure

case class EvaluationFailure(cause: Throwable) extends AuthorizationFailure
