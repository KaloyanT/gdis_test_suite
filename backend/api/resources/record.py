import os
import csv
import requests
from flask_restful import Resource, request
from flask import render_template, Response
from werkzeug.utils import secure_filename


def allowed_file(request, filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ['csv', 'txt']


class Record(Resource):

    def post(self):
        if 'file' not in request.files:
            resp = Response(response=str(request.data), status=400)
            print('fdajkfdkadgdf')
            print(request.form)
            print(request.values)
            print(request.args)
        else:
            file = request.files['file']

            if file and allowed_file(request, file.filename):
                filename = secure_filename(file.filename)
                file.save(os.path.join('/', filename))

                with open('/' + filename, 'r') as csv_file:
                    data = list(csv.DictReader(csv_file))

                # still needs to be tested, uncomment for it to work with kaloyans importer
                # requests.post("http://localhost:8083", data=data)
                resp = Response(response='File uploaded.', status=200)

            else:
                resp = Response(response='File not uploaded.', status=400)

        resp.headers['Access-Control-Allow-Origin'] = '*'
        return resp
