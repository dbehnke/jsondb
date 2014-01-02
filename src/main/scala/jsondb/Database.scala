package jsondb

import scala.collection.mutable.ListBuffer
import scala.util.parsing.json._

import scalikejdbc._, SQLInterpolation._
import spray.json._

object Database {
  def stageH2Pool = {
    Class.forName("org.h2.Driver")

    val settings = ConnectionPoolSettings(
      initialSize = 5,
      maxSize = 20,
      connectionTimeoutMillis = 3000L,
      validationQuery = "select 1 from dual")

    implicit val factory = TomcatH2ConnectionPoolFactory
    ConnectionPool.add('tomcat,"jdbc:h2:mem:hello", "user", "pass", settings)
  }

  def queryJSON(dbpool : Any, s : SQLSyntax) : String = {
    NamedDB(dbpool) readOnly { implicit session => {
      var rowListBuffer = ListBuffer[JSONObject]()
      sql"${s}" foreach { rs => {
          var colMap = Map[String, Any]()
          (1 to rs.metaData.getColumnCount) foreach { i => {
            val fieldname = rs.metaData.getColumnLabel(i)
            val fieldvalue = rs.any(i)
            colMap += (fieldname -> fieldvalue)
          } }
          rowListBuffer += JSONObject(colMap)
        }
      }
      JSONArray(rowListBuffer.toList).toString
    } }
  }

  def executeJSON(dbpool : Any, s : SQLSyntax) : String = {
    NamedDB(dbpool) autoCommit { implicit session => {
      sql"${s}".execute.apply()
      JSONObject(Map("status" -> "ok")).toString
    } }
  }

  def batchJSON(dbpool : Any, s : SQLSyntax, 
    batch : Seq[Seq[String]]) : String = {
    NamedDB(dbpool) localTx { implicit session => {
      sql"${s}".batch(batch: _*).apply()
      JSONObject(Map("status" -> "ok")).toString
    } }
  }
}