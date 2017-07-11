from flask_restful import Resource, request
from flask import jsonify, Response
import requests
import json

class EntitiesObject(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/entities').text
        return Response(response=data, mimetype='application/json')


class EntitiesMapping(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/entities/as-mappings').text
        return Response(response=data, mimetype='application/json')