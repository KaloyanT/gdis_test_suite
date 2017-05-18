from flask_restful import Resource

class RecordJson(Resource):

	def get(self):
		return 'json'