import os
import csv
import requests
from flask_restful import Resource, request
from flask import render_template, make_response
from werkzeug.utils import secure_filename


def allowed_file(request, filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ['csv', 'txt']


class Record(Resource):

    def post(self):
        if 'file' not in request.files:
            return make_response(500)
        file = request.files['file']

        if file and allowed_file(request, file.filename):
            filename = secure_filename(file.filename)
            file.save(os.path.join('/', filename))

            with open('/' + filename, 'r') as csv_file:
                data = list(csv.DictReader(csv_file))

            # still needs to be tested, uncomment for it to work with kaloyans importer
            # requests.post("http://localhost:8083", data=data)
            return 'uploaded and parsed, not yet passed to importer.'

        return '500, Bad File, maybe wrong extension?'


class Index(Resource):

    def get(self):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('index.html'), 200, headers)
