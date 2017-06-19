from flask_restful import Resource
import requests

class RecordJson(Resource):

	def get(self):
		return requests.get('http://localhost:8082/exporter/e/basicStoryTest/all')