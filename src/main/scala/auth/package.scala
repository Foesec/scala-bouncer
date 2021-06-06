package org.flxkbr.bouncer

import zio.IO

package object auth {
  type AuthIO = IO[AuthorizationFailure, Unit]
}
