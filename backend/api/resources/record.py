import os
import io
import csv
import json
import requests
from datetime import datetime
import calendar
from flask_restful import Resource, request
from flask import render_template, Response
from werkzeug.utils import secure_filename


def allowed_file(request, filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ['csv', 'txt']


class Record(Resource):

    def post(self):
        try:
            sent_data = request.data.decode('utf-8')

            reader = csv.reader(io.StringIO(sent_data), delimiter=';')
            data_list = list()

            for row in reader:
                data_list.append(row)

            keys = data_list.pop(0)
            data = [zip(keys, row) for row in data_list]

            dict_from_data = [{key: value
                              for (key, value) in row}
                              for row in data]

            d = datetime.utcnow()
            unixtime = calendar.timegm(d.utctimetuple())

            ret_arr = {
                "storyType": "basicStoryTest",
                "storyName": "newContract.T{}".format(unixtime),
                "testName": "storyExample.T{}".format(unixtime),
                "testData": dict_from_data
            }

            s = json.dumps(ret_arr)
            headers = {'Accept-Encoding': 'UTF-8', 'Content-Type': 'application/json', 'Accept': '*/*'}

            r = requests.post('http://importer:8083/importer/i/test-case', headers=headers, data=s)

            resp = Response(response=s, status=r.status_code)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
        except:
            resp = Response(response='API Error.', status=500)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
