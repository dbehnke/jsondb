package jsondb

import scalikejdbc._

/**
 * tomcat jdbc Connection Pool Factory
 */
object TomcatConnectionPoolFactory extends ConnectionPoolFactory {
  override def apply(url: String, user: String, password: String,
    settings: ConnectionPoolSettings = ConnectionPoolSettings()) = {
    new TomcatConnectionPool(url, user, password, settings)
  }
}