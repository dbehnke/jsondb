name := "jsondb"

version := "0.1.0"

scalaVersion := "2.10.3"

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.6",
  "io.spray" % "spray-routing" % "1.2.0",
  "io.spray" % "spray-caching" % "1.2.0",
  "io.spray" % "spray-can" % "1.2.0",
  "io.spray" % "spray-json_2.10" % "1.2.5",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.2.3",
  "org.scalikejdbc" %% "scalikejdbc"               % "[1.7,)",
  "org.scalikejdbc" %% "scalikejdbc-interpolation" % "[1.7,)",
  "org.scalikejdbc" %% "scalikejdbc-config"        % "[1.7,)",
  "com.h2database"  %  "h2"                        % "[1.3,)",
  "ch.qos.logback"  %  "logback-classic"           % "[1.0,)",
  "org.apache.tomcat" % "tomcat-jdbc" % "7.0.47"
)
