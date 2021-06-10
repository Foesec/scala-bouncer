package org.flxkbr.bouncer.resultauth

import zio.IO

trait RPermission[E, L, R] {
  def authorize: IO[E, Either[L, R]]
}
