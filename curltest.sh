#!/bin/bash
curl -X POST -H "Content-Type: application/json" http://localhost:8080/query -u "admin:haxor!2013" --data '{ "sql":"select * from information_schema.help","data":[],"typedef":["int"]}' -v
