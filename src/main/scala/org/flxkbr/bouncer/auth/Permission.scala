package org.flxkbr.bouncer.auth

trait Permission {
  def isAuthorized: AuthIO
}
