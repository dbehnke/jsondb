#!/bin/bash
curl -X POST -H "Content-Type: application/json" http://localhost:8080/query \
-v -u "admin:haxor!2013" \
--data '{ "sql":"select * from information_schema.help","db":"default","data":[],"typedef":["int"]}'

#curl -X POST -H "Content-Type: application/json" http://localhost:8080/query \
#-v -u "admin:haxor!2013" \
#--data '{"typedef": ["string", "string", "datetime"], "data": [], "sql": "SELECT * FROM TEST"}'
