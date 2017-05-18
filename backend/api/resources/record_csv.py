from flask_restful import Resource

class RecordCsv(Resource):

	def get(self):
		return 'csv'