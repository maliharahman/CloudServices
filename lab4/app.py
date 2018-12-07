from flask import Flask
app = Flask(__name__)
from flask import Flask, render_template ,session, escape, request, Response, jsonify
from pymongo import MongoClient

MONGO_HOST = "mongodb://127.0.0.1"
MONGO_PORT = 27017
MONGO_DB = "newDB"
MONGO_USER = "root"
MONGO_PASS = "root"
connection = MongoClient(MONGO_HOST, MONGO_PORT)
mongo = connection[MONGO_DB]

@app.route('/')
def hello():
    return "Hello World!"

@app.route('/newDB')
def fetchYear():
    stockGraphValues={}
    collection=mongo.newDB
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

@app.route('/mydbaa')
def fetchValue():
    collection=mongo.mycol
    varValue = {}
    for q in collection.find({}):
        storeID = str(q['_id']);
        varValue[storeID] = q['value']
    return jsonify(varValue)

@app.route('/mysave', methods=['POST'])
def saveData():
    name = request.json['year']
    collection=mongo.mycol
    query = {"name":name}
    abc = collection.insert(query)
    return "done"

if __name__ == '__main__':
    app.run()
