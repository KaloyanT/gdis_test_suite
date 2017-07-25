import ast
import io
import csv
import json
import requests
import random
from datetime import datetime
import calendar
from flask_restful import Resource, request
from flask import render_template, Response


class DefaultStory(Resource):

    def get(self):
        s = json.dumps({
            "storyName": "default story",
            "description": "Default Story type for Demo purposes.",
            "scenarios": ["firstParam", "secondParam"]
        })

        headers = {'Accept-Encoding': 'UTF-8', 'Content-Type': 'application/json', 'Accept': '*/*'}
        try:
            r = requests.post('http://importer:8083/importer/i/story', headers=headers, data=s)
            resp = Response(response='created default story', status=200)
            resp.headers['Access-Control-Allow-Origin'] = '*'
        except:
            resp = Response(response='API Error.', status=500)
            resp.headers['Access-Control-Allow-Origin'] = '*'
        finally:
            return resp


class ExportStories(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/stories/list').text
        return Response(response=data, mimetype='application/json')


class ExportTestNames(Resource):

    def get(self):
        data = [test.get('testName')
                for test in ast.literal_eval(requests.get('http://exporter:8082/exporter/e/storyTest/all').text)]
        return Response(response=json.dumps(data), mimetype='application/json')


class ImportStory(Resource):

    def post(self):
        try:
            sent_data = request.data.decode('utf-8')

            ret_arr = {
                'storyName': request.args.get('story_name'),
                'description': sent_data[:100],
                'scenarios': [request.args.get('scenarios')]
            }

            s = json.dumps(ret_arr)
            headers = {'Accept-Encoding': 'UTF-8', 'Content-Type': 'application/json', 'Accept': '*/*'}

            r = requests.post('http://importer:8083/importer/i/story', headers=headers, data=s)

            resp = Response(response=s, status=r.status_code)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
        except Exception as e:
            resp = Response(response=e, status=200)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
