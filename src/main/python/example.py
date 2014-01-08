from config import config
from jsondb import JsonDB
from urllib.error import HTTPError
from datetime import datetime

jsondb = JsonDB(config['uriprefix'], config['user'],
                config['passwd'], config['auth_realm'])

try:
    data = jsondb.query(
        'SELECT * FROM INFORMATION_SCHEMA.HELP WHERE ID = ?', ["327"], ["int"])
except HTTPError as e:
    print("error occured: (%d) %s" % (e.code, e.msg))

# data = jsondb.query(
#    'SELECT * FROM INFORMATION_SCHEMA.HELP')

# for d in data:
#    print("id=%s section=%s" % (d['ID'], d['SECTION']))

# data = jsondb.query(
#    'SELECT * FROM INFORMATION_SCHEMA.HELP WHERE ID = ?', ["327"])

# for row in data:
#    print("%s" % row['ID'])

try:
    jsondb.statement(
        'CREATE TABLE IF NOT EXISTS TEST ("ID" IDENTITY, ' +
        '"TESTFIELD" VARCHAR, ' +
        '"TESTFIELD2" VARCHAR, "TESTFIELD3" TIMESTAMP)')
except HTTPError as e:
    print("error occured: (%d) %s" % (e.code, e.msg))


typedef = ["string", "string", "datetime"]
data = [
    ['cheese', 'cheddar', datetime.now().isoformat()],
    ['cake', 'chocolate', datetime.now().isoformat()],
    ['butter', 'smart balance', datetime.now().isoformat()],
    ['cookies', 'windmill', datetime.now().isoformat()]]

try:
    response = jsondb.batch(
        'INSERT INTO TEST (TESTFIELD, TESTFIELD2, TESTFIELD3) ' +
        'VALUES (?, ?, ?)', data, typedef)
except HTTPError as e:
    print("error occured: (%d) %s" % (e.code, e.msg))


typedef = ["int", "string", "string", "datetime"]
try:
    data = jsondb.query(
        'SELECT * FROM TEST', [], typedef)
except HTTPError as e:
    print("error occured: (%d) %s" % (e.code, e.msg))

#data = jsondb.query('SELECT * FROM TEST')

#jsondb.statement('DROP TABLE TEST')
