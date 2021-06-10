package org.flxkbr.bouncer.auth.basic

sealed trait AuthorizationFailure

case class BasicAuthorizationCheckFailure(message: String) extends AuthorizationFailure
case class EvaluationFailure(cause: Throwable)             extends AuthorizationFailure
