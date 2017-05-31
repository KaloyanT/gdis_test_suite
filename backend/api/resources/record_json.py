from flask_restful import Resource
import requests

class RecordJson(Resource):

	def get(self):
		# Still need path to endpoint for this to work, everything else should work just finde
		return 'got your request, please fill in correct Endpoint in Code to continue.'
		# return requests.get('http://localhost:8082/e/TODO')