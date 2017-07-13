import io
import csv
import json
import requests
import random
from datetime import datetime
import calendar
from flask_restful import Resource, request
from flask import render_template, Response
import numpy as np


def allowed_file(request, filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ['csv', 'txt']


class Record(Resource):

    def post(self):

        sent_data = request.data.decode('utf-8')
        mapping = [(pair.split(',')[0], pair.split(',')[1]) for pair in request.args.get('mapping', '').split(';') if pair and len(pair.split(',')) > 1]
        mapping_real = {key: value for (key, value) in mapping}

        reader = csv.reader(io.StringIO(sent_data), delimiter=';')
        data_list = list()

        for row in reader:
            data_list.append(row)

        keys = data_list.pop(0)
        data = np.array(data_list)

        dict_from_data = [data[:,i].tolist() for i in range(len(data_list[0]))]
        zipped_data = list(zip(keys, dict_from_data))

        final_dict = [{"columnName": entry[0], "rows": entry[1]} for entry in zipped_data]

        ret_arr = {
            "storyType": "storyTest",
            "testName": request.args.get('test_name'),
            "storyName": request.args.get('story_name'),
            "mappings": mapping_real,
            "data": final_dict
        }

        s = json.dumps(ret_arr)
        headers = {'Access-Control-Allow-Origin': '*', 'Accept-Encoding': 'UTF-8', 'Content-Type': 'application/json', 'Accept': '*/*'}
        r = requests.post('http://importer:8083/importer/i/test-case', headers=headers, data=s)

        resp = Response(response=s, status=r.status_code)
        resp.headers['Access-Control-Allow-Origin'] = '*'
        return resp


class RecordDiscovery(Resource):

    def post(self):
        try:
            sent_data = request.data.decode('utf-8')

            reader = csv.reader(io.StringIO(sent_data), delimiter=';')
            data_list = list()

            for row in reader:
                data_list.append(row)

            keys = json.dumps(data_list.pop(0))
            resp = Response(response=keys, status=200)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
        except:
            resp = Response(response='API Error.', status=500)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
