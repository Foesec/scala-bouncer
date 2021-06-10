package org.flxkbr.bouncer.auth

import zio.IO

trait Permission[E] {
  def isAuthorized: IO[E, Unit]
}
