from flask_restful import Resource
from flask import jsonify, Response
import requests
import json

class RecordJson(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/basicStoryTest/all').text
        return Response(response=data, mimetype='application/json')
