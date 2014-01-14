jsondb
======

*Disclaimer: This is just a proof of concept

Create an HTTP interface to any JDBC capable database - Uses JSON requests and responses

Provides a non-validating access to the database, with basic authentication.  It's meant to be used as a layer between mid-tier and the database.  You shouldn't allow access to the jsondb application directly except for testing.


Use Cases
---------

* Lightweight communication with database from scripts

I would recommend not putting the webserver on a public interface, instead have a public API with secure functions that talk to jsondb as a backend.

Build Requirements
------------------

* JDK 7

Demo
----

The demo uses a Python script to talk to jsondb.. You'll need Python 3.3+ installed to run it.

1. Startup the webserver (uses port 8080 by default)

        $ sbt/sbt run
  
2. Run the example python script

        $ cd src/main/python
        $ python example.py

Deployment
----------

You can package everything into a single "uberjar":

    $ sbt/sbt one-jar

To run:

    $ java -jar target/scala-2.10/jsondb_2.10-0.1.0-one-jar.jar

Technology Used
---------------

* scala - http://scala-lang.org

* scalikejdbc - http://scalikejdbc.org/

* tomcat jdbc-pool - http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html

* spray - http://spray.io

* H2 (for demo database) - http://www.h2database.com/
