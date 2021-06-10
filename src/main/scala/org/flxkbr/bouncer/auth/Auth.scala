package org.flxkbr.bouncer.auth

import org.flxkbr.bouncer.auth.basic.{AuthorizationFailure, BooleanTotalPermission}
import zio.{Has, IO, ULayer, ZIO, ZLayer}

trait Auth {
  def authorize[E](permission: Permission[E]): IO[E, Unit]
}

object Auth {

  def authorize[E](permission: Permission[E]): ZIO[Has[Auth], E, Unit] =
    ZIO.serviceWith[Auth](_.authorize(permission))

  object basic {
    def authorizeTotal(
        check: => Boolean,
        failureMessage: String
    ): ZIO[Has[Auth], AuthorizationFailure, Unit] =
      ZIO.serviceWith[Auth](_.authorize(BooleanTotalPermission(() => check, failureMessage)))
  }

}

case class AuthLive() extends Auth {
  override def authorize[E](permission: Permission[E]): IO[E, Unit] =
    permission.isAuthorized
}

object AuthLive {
  val layer: ULayer[Has[Auth]] = ZLayer.succeed(AuthLive())
}
