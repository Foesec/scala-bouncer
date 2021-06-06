package org.flxkbr.bouncer
package auth

import auth.basic.BooleanTotalPermission

import zio.{Has, ULayer, ZIO, ZLayer}

trait Auth {
  def authorize(permission: Permission): AuthIO
}

object Auth {

  def authorize(permission: Permission): ZIO[Has[Auth], AuthorizationFailure, Unit] =
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
  override def authorize(permission: Permission): AuthIO =
    permission.isAuthorized
}

object AuthLive {
  val layer: ULayer[Has[Auth]] = ZLayer.succeed(AuthLive())
}
