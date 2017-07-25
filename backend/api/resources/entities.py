from flask_restful import Resource, request
from flask import jsonify, Response
import requests
import json
import ast


class EntitiesObject(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/entities').text
        return Response(response=data, mimetype='application/json')


class EntitiesMapping(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/entities/as-mappings').text
        return Response(response=data, mimetype='application/json')


class EntityCreation(Resource):

    def post(self):
        try:
            sent_data = request.data.decode('utf-8')
            if request.args.get('update') == 'true':
                ret_arr = {
                    "oldEntityName": request.args.get('old_entity_name'),
                    "newEntitiyName": request.args.get('new_entity_name'),
                    "oldTestEntityAttributes": request.args.get('old_attributes').split(','),
                    "newTestEntityAttributes": request.args.get('new_attributes').split(',')
                }
            else:
                ret_arr = {
                    "entityName": request.args.get('entity_name'),
                    "testEntityAttributes": request.args.get('attributes').split(',')
                }

            s = json.dumps(ret_arr)
            headers = {'Accept-Encoding': 'UTF-8', 'Content-Type': 'application/json', 'Accept': '*/*'}

            if request.args.get('update') == 'true':
                r = requests.put('http://importer:8083/importer/u/entity', headers=headers, data=s)
            else:
                r = requests.post('http://importer:8083/importer/i/entity', headers=headers, data=s)

            resp = Response(response=s, status=r.status_code)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
        except Exception as e:
            resp = Response(response=e, status=200)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp


class EntityCount(Resource):

    def get(self, entity):
        data = ast.literal_eval(requests.get('http://exporter:8082/exporter/e/objects/by-entity-type/' + entity).text)
        return Response(response=str(len(data)))
