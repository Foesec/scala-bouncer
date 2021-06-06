package org.flxkbr.bouncer
package auth

trait Permission {
  def isAuthorized: AuthIO
}
