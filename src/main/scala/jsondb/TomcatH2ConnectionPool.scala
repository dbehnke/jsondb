package jsondb

import java.sql.Connection
import scalikejdbc._

class TomcatH2ConnectionPool(
  override val url: String,
  override val user: String,
  password: String,
  override val settings: ConnectionPoolSettings = ConnectionPoolSettings())
  extends ConnectionPool(url, user, password, settings) {

  
  //import org.apache.tomcat.jdbc.pool.PoolProperties

  private[this] val _dataSource = new org.apache.tomcat.jdbc.pool.DataSource()
  _dataSource.setUrl(url)
  _dataSource.setDriverClassName("org.h2.Driver")
  _dataSource.setUsername(user)
  _dataSource.setPassword(password)
  _dataSource.setInitialSize(settings.initialSize)
  _dataSource.setMaxActive(settings.maxSize)
  _dataSource.setMaxWait(settings.connectionTimeoutMillis.toInt)
  //_dataSource.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
  //                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer")

  override def dataSource: org.apache.tomcat.jdbc.pool.DataSource = _dataSource
  override def borrow(): Connection = dataSource.getConnection()
  override def numActive: Int = _dataSource.getNumActive()
  override def numIdle: Int = _dataSource.getNumIdle()
  override def maxActive: Int = _dataSource.getMaxActive
  override def maxIdle: Int = _dataSource.getMaxIdle
  override def close(): Unit = _dataSource.close()
}