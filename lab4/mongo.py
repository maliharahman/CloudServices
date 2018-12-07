from flask import Flask
app = Flask(__name__)
from flask import Flask, render_template ,session, escape, request, Response, jsonify
from pymongo import MongoClient

client =MongoClient('localhost')
print(client)
db=client.newDB
db.newDB.count()
newDB=db.clients
newDB.find()


import httplib2 as http
import json

try:
    from urlparse import urlparse
except ImportError:
    from urllib.parse import urlparse

headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json; charset=UTF-8'
}

uri = 'http://ipaddress:8080'
path = '/app/stock/'

target = urlparse(uri+path)
method = 'GET'
body = ''

h = http.Http()


response, content = h.request(
        target.geturl(),
        method,
        body,
        headers)

# assume that content is a json reply
# parse content with the json module
data = json.loads(content)
data_list= []

for item in data:
    data_object={'name':None,'id':None,'value':None}
    data_object['id']=item['id']
    data_object['name']=item['name']
    data_object['value']=item['value']
    data_list.append(data_object)


print(data)
print(data_list)

for object in data_list:
    newDB=db.newDB.insert({"id": object['id'] ,"name" : object['name'], "value": object['value']})
    print(object['name'])
    print(object)
