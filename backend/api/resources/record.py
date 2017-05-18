from flask_restful import Resource, request
from flask import render_template, make_response


class Record(Resource):

    def post(self):
        return str(request.files['file'])


class Index(Resource):

    def get(self):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('index.html'), 200, headers)
