from config import config
from jsondb import JsonDB

jsondb = JsonDB(config['uriprefix'], config['user'],
                config['passwd'], config['auth_realm'])

data = jsondb.query(
    'SELECT * FROM INFORMATION_SCHEMA.HELP WHERE ID = ?', ["327"], ["string"])

# data = jsondb.query(
#    'SELECT * FROM INFORMATION_SCHEMA.HELP WHERE ID = ?', ["327"])

# for row in data:
#    print("%s" % row['ID'])

jsondb.statement(
    'CREATE TABLE IF NOT EXISTS TEST ("ID" IDENTITY, "TESTFIELD" VARCHAR, ' +
    '"TESTFIELD2" VARCHAR, "TESTFIELD3" BIGINT)')

testbatchinsert = [
    ['cheese', 'cheddar', str(222)], ['cake', 'chocolate', str(3333)],
    ['butter', 'smart balance', str(4444)],
    ['cookies', 'windmill', str(5555)]]

data = jsondb.batch(
    'INSERT INTO TEST (TESTFIELD, TESTFIELD2, TESTFIELD3) VALUES (?, ?, ?)',
    testbatchinsert)

data = jsondb.query('SELECT * FROM TEST')

jsondb.statement('DROP TABLE TEST')
