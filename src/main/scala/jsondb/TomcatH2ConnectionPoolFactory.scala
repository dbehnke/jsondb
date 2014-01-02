package jsondb

import scalikejdbc._

/**
 * tomcat jdbc Connection Pool Factory
 */
object TomcatH2ConnectionPoolFactory extends ConnectionPoolFactory {
  override def apply(url: String, user: String, password: String,
    settings: ConnectionPoolSettings = ConnectionPoolSettings()) = {
    new TomcatH2ConnectionPool(url, user, password, settings)
  }
}