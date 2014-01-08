package jsondb

import scalikejdbc._, SQLInterpolation._
import spray.json._

import org.json4s._
import org.json4s.native.JsonMethods._
import scala.collection.mutable.ListBuffer
import org.joda.time._

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

/*
case class JString(s: String) extends JValue
case class JDouble(num: Double) extends JValue
case class JDecimal(num: BigDecimal) extends JValue
case class JInt(num: BigInt) extends JValue
case class JBool(value: Boolean) extends JValue
*/

  def queryJSON(dbpool : Any, s : SQLSyntax,
   typedefs : Seq[String]) : String = {
    NamedDB(dbpool) readOnly { implicit session => {
      val rowListBuffer = ListBuffer[JObject]()
      sql"${s}" foreach { rs => {
        rowListBuffer.append(JObject((
          for (i <- 1 to rs.metaData.getColumnCount)
          yield {
            val fieldname = rs.metaData.getColumnLabel(i)
            //use string if no typedef defined 
            val typedef = {
              if (typedefs.length >= i) typedefs(i-1)
              else "string"
            }
            val fieldvalue = typedef match {
              case "string" => {
                rs.stringOpt(i) match {
                  case Some(t) => JString(t)
                  case _ => JNull
                }
              }
              case "datetime" => {
                rs.dateTimeOpt(i) match {
                  case Some(t) => JString(t.toString)
                  case _ => JNull
                }
              }
              case "int" => {
                rs.longOpt(i) match {
                  case Some(t) => JInt(t)
                  case _ => JNull
                }
              }
              case "double" => {
                rs.doubleOpt(i) match {
                  case Some(t) => JDouble(t)
                  case _ => JNull
                }
              }
              case "decimal" => {
                rs.bigDecimalOpt(i) match {
                  case Some(t) => JDecimal(t)
                  case _ => JNull
                }
              }
              case "boolean" => {
                rs.booleanOpt(i) match {
                  case Some(t) => JBool(t)
                  case _ => JNull
                }
              }
              case _ => JNull
            }
            (fieldname -> fieldvalue)
          } ).toList))
      } }
      compact(render(JArray(rowListBuffer.toList)))
    } } 
  }

  def executeJSON(dbpool : Any, s : SQLSyntax) : String = {
    NamedDB(dbpool) autoCommit { implicit session => {
      sql"${s}".execute.apply()
      compact(render(JObject(("status",JString("ok")))))
    } }
  }

  def batchJSON(dbpool : Any, s : SQLSyntax, 
    batch : Seq[Seq[String]], typedef: Seq[String]) : String = {
    val xbatch: Seq[Seq[Any]] = (for (row <- batch) yield {
      (for(i <- 0 to row.length - 1)
      yield {
        if (i <= typedef.length)
          if (batch(i).equals("none")) None  
          else typedef(i) match {
            case "datetime" => new DateTime(row(i))
            case _ => row(i).asInstanceOf[String] 
          }
        else row(i)
      })
    })
    NamedDB(dbpool) localTx { implicit session => {
      sql"${s}".batch(xbatch: _*).apply()
      compact(render(JObject(("status",JString("ok")))))
    } }
  }
}