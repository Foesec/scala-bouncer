package org.flxkbr.bouncer.auth.basic

import org.flxkbr.bouncer.auth.AuthorizationFailure

case class BasicAuthorizationFailure(message: String) extends AuthorizationFailure

case class EvaluationFailure(cause: Throwable) extends AuthorizationFailure
