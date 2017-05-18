from flask import Flask
from flask_restful import Api
from resources import record, record_json, record_csv

app = Flask(__name__, template_folder='templates')
api = Api(app)

api.add_resource(record.Index, '/')
api.add_resource(record.Record, '/record')
api.add_resource(record_json.RecordJson, '/record/json')
api.add_resource(record_csv.RecordCsv, '/record/csv')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
