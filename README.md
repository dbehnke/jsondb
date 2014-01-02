jsondb
======

Create an HTTP interface to any JDBC capable database - Uses JSON requests and responses


Use Cases
---------

* Lightweight communication with database from scripts

I would recommend not putting the webserver on a public interface, instead have a public API with secure functions that talk to jsondb as a backend.

Build Requirements
------------------

* Must have sbt (Simple-Build-Tool) installed

* JDK 7

* Python 3.3+ (Optional to run the example.py script)

Demo
----

1. Startup the webserver (uses port 8080 by default)

        $ sbt run
  
2. Run the example python script

        $ cd src/main/python
        $ python example.py
  
Technology Used
---------------

* scala - http://scala-lang.org

* scalikejdbc - http://scalikejdbc.org/

* spray - http://spray.io

* H2 (for demo database) - http://www.h2database.com/
