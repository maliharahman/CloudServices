from flask import Flask
app = Flask(__name__)
from flask import Flask, render_template ,session, escape, request, Response, jsonify
from pymongo import MongoClient
import hashlib, uuid
from flask import send_file, make_response, abort

import sys, os
MONGO_HOST = "mongodb://<dbuser>:<dbpassword>@ds129914.mlab.com:29914/newdb"
MONGO_PORT = 29914
MONGO_DB = ""
MONGO_USER = ""
MONGO_PASS = ""
connection = MongoClient(MONGO_HOST, MONGO_PORT)
mongo = connection[MONGO_DB]

@app.route('/')
def hello():
    return make_response(open('index.html').read())

@app.route('/form')
def form():
    return make_response(open('form.html').read())

@app.route('/clients', methods=['POST'])
def clients():
    firstName = request.json['fname']
    lastName = request.json['lname']
    pwd = request.json['password']
    salt = uuid.uuid4().hex
    hashed_password = hashlib.sha512(pwd + salt).hexdigest()
    query = {"first":firstName, "last":lastName, "password":hashed_password}
    collection = mongo.clients
    collection.insert(query)
    return jsonify({"status":"success"})

@app.route('/abc')
def fetchYear():
    stockGraphValues={}
    collection=mongo.mycol
    print(collection)
    for q in collection.find({}):
        temp = {}
        storeID= q['id'];
        temp['id']= q['id'];
        temp['name'] = q['name']
        temp['value'] = q['value']
        stockGraphValues[storeID] = temp

    print(stockGraphValues)
    return jsonify(stockGraphValues)

if __name__ == '__main__':
    app.run(host=os.getenv('IP', '0.0.0.0'),port=int(os.getenv('PORT', 5000)))
                                                                                                 

