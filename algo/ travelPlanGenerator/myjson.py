import json
from flask import Response


def makeReponse(message, code, data):
    data = dict(code=code, message=message, data=data)
    json_string = json.dumps(data, ensure_ascii=False)
    response = Response(json_string, content_type="application/json; charset=utf-8")
    return response
