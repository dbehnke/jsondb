import urllib.request
import json

# turn off the unquoted warning from auth_realm
import warnings
warnings.filterwarnings("ignore", category=UserWarning, module='urllib')


class JsonDB(object):

    '''
    Jsondb helper routines
    '''
    __user = None
    __passwd = None
    uri = None
    auth_realm = None

    def __init__(self, uri, user, passwd, auth_realm='db-security-realm'):
        '''
        Constructor
        '''
        self.__user = user
        self.__passwd = passwd
        self.uri = uri
        self.auth_realm = auth_realm
        auth_handler = urllib.request.HTTPBasicAuthHandler()
        auth_handler.add_password(realm=self.auth_realm,
                                  uri=self.uri,
                                  user=self.__user,
                                  passwd=self.__passwd)
        opener = urllib.request.build_opener(auth_handler)
        # ...and install it globally so it can be used with urlopen.
        urllib.request.install_opener(opener)

    def Jsondb(self):
        return self

    def query(self, sql, data=[]):
        body = {"sql": sql, "data": data}
        req = urllib.request.Request(
            self.uri + "/query", data=json.dumps(body).encode('utf-8'),
            headers={'Content-Type': 'application/json'})
        print("[DEBUG]: request body = %s" % json.dumps(body))
        resp = urllib.request.urlopen(req)
        body = resp.read()
        print("[DEBUG]: response body = %s" % body)
        return json.loads(body.decode('utf-8'))

    def statement(self, sql, data=[]):
        body = {"sql": sql, "data": data}
        req = urllib.request.Request(
            self.uri + "/statement", data=json.dumps(body).encode('utf-8'),
            headers={'Content-Type': 'application/json'})
        print("[DEBUG]: request body = %s" % json.dumps(body))
        resp = urllib.request.urlopen(req)
        body = resp.read()
        print("[DEBUG]: response body = %s" % body)
        return json.loads(body.decode('utf-8'))

    def batch(self, sql, data):
        body = {"sql": sql, "data": data}
        req = urllib.request.Request(
            self.uri + "/batch", data=json.dumps(body).encode('utf-8'),
            headers={'Content-Type': 'application/json'})
        print("[DEBUG]: request body = %s" % json.dumps(body))
        resp = urllib.request.urlopen(req)
        body = resp.read()
        print("[DEBUG]: response body = %s" % body)
        return json.loads(body.decode('utf-8'))
