from flask_restful import Resource, request
from flask import jsonify, Response, send_file
import requests
import json
import ast
from tinydb import TinyDB, where
import pandas as pd
import urllib.parse
import itertools

db = TinyDB('./db.json')


class EntitiesObject(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/entities').text
        return Response(response=data, mimetype='application/json')


class EntitiesMapping(Resource):

    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/entities/as-mappings').text
        return Response(response=data, mimetype='application/json')


class EntityCreation(Resource):

    def post(self):
        try:
            sent_data = request.data.decode('utf-8')
            if request.args.get('update') == 'true':
                ret_arr = {
                    "oldEntityName": request.args.get('old_entity_name'),
                    "newEntitiyName": request.args.get('new_entity_name'),
                    "oldTestEntityAttributes": request.args.get('old_attributes').split(','),
                    "newTestEntityAttributes": request.args.get('new_attributes').split(',')
                }
            else:
                ent_name = request.args.get('entity_name')
                attr = request.args.get('attributes').split(',')
                meta = request.args.get('metatypes').split(',')

                for attr, meta in zip(attr, meta):
                    db.insert({f'{ent_name}.{attr}': meta})

                ret_arr = {
                    "entityName": ent_name,
                    "testEntityAttributes": attr
                }
                ret_arr = {
                    "entityName": request.args.get('entity_name'),
                    "testEntityAttributes": request.args.get('attributes').split(',')
                }

            s = json.dumps(ret_arr)
            headers = {'Accept-Encoding': 'UTF-8', 'Content-Type': 'application/json', 'Accept': '*/*'}

            if request.args.get('update') == 'true':
                r = requests.put('http://importer:8083/importer/u/entity', headers=headers, data=s)
            else:
                r = requests.post('http://importer:8083/importer/i/entity', headers=headers, data=s)

            resp = Response(response=s, status=r.status_code)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp
        except Exception as e:
            resp = Response(response=e, status=200)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp


class EntityCount(Resource):
    def get(self, entity):
        data = ast.literal_eval(requests.get('http://exporter:8082/exporter/e/objects/by-entity-type/' + entity).text)
        return Response(response=str(len(data)))


class EntityGetAll(Resource):
    def get(self):
        data = requests.get('http://exporter:8082/exporter/e/entities').text
        return Response(response=data, mimetype='application/json')


class EntityGetAllByTestname(Resource):
    def get(self, testname):
        data = ast.literal_eval(requests.get('http://exporter:8082/exporter/e/storyTests/by-test-name/' + testname).text)[0].get('data', {})
        if len(data) > 0:
            attr = []
            for row in data:
                _attr = row.get('entityName') + '.' + row.get('columnName')
                if _attr not in attr:
                    attr += [_attr]
            else:
                return Response(response=json.dumps(attr), mimetype='application/json')
        else:
            return Response(response=json.dumps([]), mimetype='application/json')


class EntityDataGetFilterByTestname(Resource):
    def get(self, testname, filters, estimate):
        filters_ = json.loads(urllib.parse.unquote(filters))
        data = ast.literal_eval(requests.get('http://exporter:8082/exporter/e/storyTests/by-test-name/' + testname).text)[0].get('data')
        index = []
        unproc_data = []
        for row in data:
            index += [row['entityName'] + '.' + row['columnName']]
            unproc_data += [row['rows']]
        else:
            df = pd.DataFrame(unproc_data, index=index).T

        for f in filters_:
            _col = f['col']
            _type = f['type']
            if _type == 'string':
                df = df[df[_col].str.contains(f['exp'], regex=True)]
            if _type == 'number':
                _max = f.get('max')
                _min = f.get('min')
                if _max:
                    df = df[df[_col] <= str(_max)]
                if _min:
                    df = df[df[_col] >= str(_min)]
            if _type == 'location':
                pass

        if estimate == 'true':
            return Response(response=json.dumps(len(df)))
        elif estimate == 'false':
            csv = df.to_csv(sep=';', index=False)
            resp = Response(response=csv, mimetype='text/csv')
            resp.headers['Content-Disposition'] = 'attachment; filename=test_data.csv'
            return resp
        elif estimate == 'json':
            resp = Response(response=df.to_json(), mimetype='application/json')


class EntityGetFilteredData(Resource):
    def get(self, filter_mode, attrs_raw, filters_raw, mode, test_name):
        filters = json.loads(urllib.parse.unquote(filters_raw))
        attributes = ast.literal_eval(attrs_raw)

        if filter_mode == 'single':
            data = [ast.literal_eval(requests.get('http://exporter:8082/exporter/e/entities/values-for-attribute/' +
                    attribute.split('.')[0] + '/' + attribute.split('.')[1]).text)
                    for attribute in attributes]
            dfs = [pd.DataFrame(d, columns=[attributes[idx]]) for idx, d in enumerate(data)]

        if filter_mode == 'entity':
            ents = list({a.split('.')[0] for a in attributes})
            data = []
            grouped_attributes = []
            for ent in ents:
                grouped_attributes.append([attr for attr in attributes if ent == attr.split('.')[0]])
            for attr_group in grouped_attributes:
                _d = [ast.literal_eval(requests.get('http://exporter:8082/exporter/e/entities/values-for-attribute/' +
                      attribute.split('.')[0] + '/' + attribute.split('.')[1]).text)
                      for attribute in attr_group]
                data += [_d]
            dfs = [pd.DataFrame(d, index=grouped_attributes[idx]).T for idx, d in enumerate(data)]

        if filter_mode == 'test':
            unproc_data = ast.literal_eval(requests.get('http://exporter:8082/exporter/e/storyTests/by-test-name/' + test_name).text)[0].get('data')
            index = []
            data = []
            for row in unproc_data:
                index += [row['entityName'] + '.' + row['columnName']]
                data += [row['rows']]

            dfs = [pd.DataFrame(data, index=index).T]

            attributes = dfs[0].keys().tolist()
            ents = list({a.split('.')[0] for a in attributes})
            grouped_attributes = []
            for ent in ents:
                grouped_attributes.append([attr for attr in attributes if ent == attr.split('.')[0]])

        res = []
        for d in dfs:
            for f in filters:
                _col = f['col']
                if _col in d:
                    _type = f['type']
                    if _type == 'string':
                        d = d[d[_col].str.contains(f['exp'], regex=True)]
                    if _type == 'number':
                        _max = f.get('max')
                        _min = f.get('min')
                        if _max:
                            d = d[d[_col] <= str(_max)]
                        if _min:
                            d = d[d[_col] >= str(_min)]
                    if _type == 'location':
                        pass
            res.append(d)

        if mode == 'count':
            lengths = [len(d) for d in res]
            resp = Response(response=json.dumps(lengths), mimetype='application/json')
            return resp

        if mode == 'json':
            if filter_mode == 'test':
                d = []
                for ent, group in zip(ents, grouped_attributes):
                    d.append({'name': ent, 'attributes': group, 'data': res[0][group].values.tolist(), 'count': len(res[0][group].values.tolist())})
                d = json.dumps(d)
            else:
                d = json.dumps([{'name': e, 'attributes': g, 'data': r.values.tolist(), 'count': len(r.values.tolist())}
                                for g, e, r in zip(grouped_attributes, ents, res)])
            resp = Response(response=d, mimetype='application/json')
            return resp

        if mode == 'csv':
            if mode == 'test':
                csv = res[0].to_csv(sep=';', index=False)
                resp = Response(response=csv, mimetype='text/csv')
                resp.headers['Content-Disposition'] = 'attachment; filename=test_data.csv'
                return resp

        else:
            # ToDo: Recombination
            if mode == 'csv':
                csv = res[0].to_csv(sep=';', index=False)  # change
                resp = Response(response=csv, mimetype='text/csv')
                resp.headers['Content-Disposition'] = 'attachment; filename=test_data.csv'
                return resp
            if mode == 'json':
                resp = Response(response=json.dumps(res[0].to_json()), mimetype='application/json')  # change
                return resp


class EntityMeta(Resource):
    def get(self, attribute):
        try:
            res = db.search(where(attribute).exists())[0][attribute]
        except:
            res = 'string'
        return Response(response=res)


class EntityGetAttributesByEntity(Resource):
    def get(self, entity):
        data = ast.literal_eval(requests.get('http://exporter:8082/exporter/e/entities/as-mappings').text)
        res = []
        for m in data:
            c = m.split('.')[0]
            if entity == c:
                res += [m]
        return Response(response=json.dumps(res), mimetype='application/json')


class EntityDownload(Resource):
    def post(self, mode):
        try:
            sent_data = json.loads(request.data.decode('utf-8'))
            recomb = True if mode == 'true' else False
            if not recomb:
                df = pd.DataFrame(sent_data[0])
                return Response(str(df.to_csv(encoding='utf-8', sep=';'))[1:], mimetype="text/csv", headers={"Content-disposition": "attachment; filename=testCase.csv"})

        except Exception as e:
            resp = Response(response=e, status=200)
            resp.headers['Access-Control-Allow-Origin'] = '*'
            return resp

        csv = f'{mode},1,2,3\n4,5,6\n'

