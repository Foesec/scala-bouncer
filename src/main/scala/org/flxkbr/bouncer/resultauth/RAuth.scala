package org.flxkbr.bouncer.resultauth

import zio.{Has, IO, ZIO}

trait RAuth {
  def authorize[E, L, R](permission: RPermission[E, L, R]): IO[E, Either[L, R]]

  def foldResult[E, L, R, ZR, A](permission: RPermission[E, L, R])(
      onSuccess: (R) => ZIO[ZR, E, A],
      onFailure: (L) => ZIO[ZR, E, A]
  ): ZIO[ZR, E, A]
}

object RAuth {

  def authorize[E, L, R](permission: RPermission[E, L, R]): ZIO[Has[RAuth], E, Either[L, R]] =
    ZIO.serviceWith[RAuth](_.authorize(permission))

}

case class RAuthLive() extends RAuth {
  override def authorize[E, L, R](permission: RPermission[E, L, R]): IO[E, Either[L, R]] =
    permission.authorize

  override def foldResult[E, L, R, ZR, A](
      permission: RPermission[E, L, R]
  )(onSuccess: R => ZIO[ZR, E, A], onFailure: L => ZIO[ZR, E, A]): ZIO[ZR, E, A] =
    for {
      authResult <- permission.authorize
      mapped     <- authResult.fold(onFailure, onSuccess)
    } yield mapped
}
