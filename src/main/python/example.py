from config import config
from jsondb import JsonDB
from urllib.error import HTTPError

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
        '"TESTFIELD2" VARCHAR, "TESTFIELD3" BIGINT)')
except HTTPError as e:
    print("error occured: (%d) %s" % (e.code, e.msg))


typedef = ["string", "string", "long"]
data = [
    ['cheese', 'cheddar', str(222)], ['cake', 'chocolate', str(3333)],
    ['butter', 'smart balance', str(4444)],
    ['cookies', 'windmill', str(5555)]]

try:
    response = jsondb.batch(
        'INSERT INTO TEST (TESTFIELD, TESTFIELD2, TESTFIELD3) ' +
        'VALUES (?, ?, ?)', data, typedef)
except HTTPError as e:
    print("error occured: (%d) %s" % (e.code, e.msg))


try:
    data = jsondb.query(
        'SELECT * FROM TEST')
except HTTPError as e:
    print("error occured: (%d) %s" % (e.code, e.msg))

#data = jsondb.query('SELECT * FROM TEST')

#jsondb.statement('DROP TABLE TEST')
