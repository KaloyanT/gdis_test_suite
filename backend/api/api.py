from flask import Flask
from flask_restful import Api
from flask_cors import cross_origin, CORS
from resources import record, record_json, record_csv, entities, stories
import requests
import json
import time

UPLOAD_FOLDER = '/'
ALLOWED_EXTENSIONS = set(['csv', 'txt'])


app = Flask(__name__, template_folder='templates')
CORS(app)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['ALLOWED_EXTENSIONS'] = ALLOWED_EXTENSIONS
api = Api(app)

api.add_resource(record.Record, '/record')
api.add_resource(record.RecordDiscovery, '/record/discovery')
api.add_resource(record_json.RecordJson, '/record/json')
api.add_resource(record_csv.RecordCsv, '/record/csv')
api.add_resource(entities.EntitiesObject, '/entities/object')
api.add_resource(entities.EntitiesMapping, '/entities/mapping')
api.add_resource(entities.EntityCreation, '/entity')
api.add_resource(stories.DefaultStory, '/stories/default')
api.add_resource(stories.ExportStories, '/stories/list')
api.add_resource(stories.ImportStory, '/story')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
