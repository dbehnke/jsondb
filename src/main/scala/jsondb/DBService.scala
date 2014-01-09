package jsondb

import akka.actor._
import spray.routing.{HttpService, RequestContext}
import spray.json._
import scalikejdbc._, SQLInterpolation._
import spray.json.DefaultJsonProtocol
//import spray.http.HttpHeaders._
import spray.http.MediaTypes._
import spray.routing.authentication.BasicAuth
import spray.routing.authentication.UserPass
import spray.routing.authentication.UserPassAuthenticator
import spray.routing.authentication.UserPassAuthenticator
import scala.concurrent.Promise
import scala.util.parsing.json._
import spray.http.StatusCodes._
import spray.util.LoggingContext

//import spray.httpx.SprayJsonSupport._

class DBServiceActor extends Actor with DBService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing,
  // timeout handling or alternative handler registration
  def receive = runRoute(dbRoute)
}

case class QueryStatement(sql: String, typedef: Seq[String],
  data: Seq[String], db: String)
case class ExecuteStatement(sql: String, data: Seq[String], db: String)
case class BatchStatement(sql : String, data: Seq[Seq[String]],
  typedef: Seq[String], db: String)

//case class Result(rows: List[Map[String,Any]])
//case class Result(rows: String)

//object ResultJsonSupport extends DefaultJsonProtocol {
//  implicit val PortofolioFormats = jsonFormat1(Result)
//}

object QueryJsonSupport extends DefaultJsonProtocol {
   implicit val PortofolioFormats = jsonFormat4(QueryStatement)
}

object ExecuteJsonSupport extends DefaultJsonProtocol {
   implicit val PortofolioFormats = jsonFormat3(ExecuteStatement)
}

object BatchDDLJsonSupport extends DefaultJsonProtocol {
  implicit val PortofolioFormats = jsonFormat4(BatchStatement)
}


import spray.httpx.SprayJsonSupport._

trait DBService extends HttpService {
  // database stuff
  implicit val session = AutoSession

  //authentication stuff
  case class UserProfile(name: String)

  def getUserProfile(name: String, password: String): Option[UserProfile] = {
    if (name == JsondbGlobal.httpUser && password == JsondbGlobal.httpPass)
      Some(UserProfile(s"$name"))
    else
      None
  }

  object CustomUserPassAuthenticator extends UserPassAuthenticator[UserProfile] {
    def apply(userPass: Option[UserPass]) = Promise.successful(
      userPass match {
        case Some(UserPass(user, pass)) => {
          getUserProfile(user, pass)
        }
        case _ => None
      }).future
  }

  // we use the enclosing ActorContext's or ActorSystem's dispatcher for our Futures and Scheduler
  implicit def executionContext = actorRefFactory.dispatcher

  val dbRoute = {
    authenticate(BasicAuth(CustomUserPassAuthenticator, "db-security-realm"))
     { userProfile => {
      path(JsondbGlobal.httpUrlPrefix / "query") {
        post {
          import QueryJsonSupport._
          entity(as[QueryStatement]) { query => { 
            respondWithMediaType(`application/json`) {
              detach() {
                try {
                  val x = Database.queryJSON(query.db,
                    sqls"${UnsafeSQLSyntax(query.sql,query.data)}",
                    query.typedef)
                  complete(x)
                } catch {
                  case e : Exception => {
                    complete(BadRequest,JsObject(
                      Map("status" -> JsString(e.getMessage))).compactPrint)
                  }
                }
              }
            }
          }}
        }
      } ~
      path(JsondbGlobal.httpUrlPrefix / "execute") {
        post {
          import ExecuteJsonSupport._
          entity(as[ExecuteStatement]) { statement => { 
            respondWithMediaType(`application/json`) {
              detach() {
                try {
                  val x = Database.executeJSON(statement.db, 
                  sqls"${UnsafeSQLSyntax(statement.sql,statement.data)}")
                  complete(x)
                } catch {
                  case e : Exception => {
                    complete(BadRequest,JsObject(
                      Map("status" -> JsString(e.getMessage))).compactPrint)
                  }
                }
              }
            }
          }}
        }
      } ~
      path(JsondbGlobal.httpUrlPrefix / "batch") {
        post {
          import BatchDDLJsonSupport._
          entity(as[BatchStatement]) { bs => { 
            respondWithMediaType(`application/json`) {
              detach() {
                try {
                  val x = Database.batchJSON(bs.db, 
                  sqls"${UnsafeSQLSyntax(bs.sql,Seq[String]())}", bs.data,
                  bs.typedef)
                  complete(x)
                } catch {
                  case e : Exception => {
                    complete(BadRequest,JsObject(
                      Map("status" -> JsString(e.getMessage))).compactPrint)
                    
                  }
                }
              }
            }
          }}
        }
      }
    } }
  }
}
