package jsondb

import com.typesafe.config._

object JsondbGlobal {
  var httpInterface = "localhost" 
  var httpPort = 8080
  var httpUser = "admin"
  var httpPass = "haxor!2013"
  var httpUrlPrefix = "jsondb"

  def initFromTypesafeConfig = {
    val config = ConfigFactory.load
    val httpConfig = config.getConfig("jsondb.http")
    httpInterface = { 
      if (httpConfig.hasPath("interface"))
        httpConfig.getString("interface")
      else "localhost"
    }
    httpPort = { 
      if (httpConfig.hasPath("port"))
        httpConfig.getInt("port")
      else 8080
    }
    httpUser = { 
      if (httpConfig.hasPath("user"))
        httpConfig.getString("user")
      else "admin"
    }
    httpPass = { 
      if (httpConfig.hasPath("pass"))
        httpConfig.getString("pass")
      else "haxor!2013"
    }
    httpUrlPrefix = { 
      if (httpConfig.hasPath("urlPrefix"))
        httpConfig.getString("urlPrefix")
      else "jsondb"
    }
  }
}