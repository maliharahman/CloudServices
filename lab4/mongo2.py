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

for k in range(5):
    newDB=db.newDB.insert({"id" : 2, "name" : "Dow", "value": 1111})
print(k)
