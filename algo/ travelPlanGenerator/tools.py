import json
import os

import httpx
from dotenv import load_dotenv
load_dotenv(dotenv_path='./config.env')


def get_current_weather(loc):
    """
    查询即时天气函数
    :param loc:必要参数，字符串类型。用于表示查询天气的具体城市名称，\
    注意，中国的城市要用对应城市的英文名称代替，例如如果需要查询北京市的天气，则loc参数需要输入为'Beijing'
    :return OpenWeather API查询即时天气的结果，具体url请求地址为"https://api.openweathermap.org/data/2.5/weather"
    返回结果为对象类型解析后的JSON格式对象，并用字符串形式进行表示，其中包含了全部重要的天气信息
    """
    url = "https://api.openweathermap.org/data/2.5/weather"
    params = {
        "q": loc,
        "appid": os.getenv("WEATHER_KEY"),
        "units": "metric",
        "lang": "zh_cn"
    }

    # 使用 httpx.Client 和相关设置
    with httpx.Client(proxies="http://localhost:7890",
                      transport=httpx.HTTPTransport(local_address="0.0.0.0")) as client:
        response = client.get(url, params=params)
        data = response.json()
    return json.dumps(data)

def searchTravelPol(keywords=None, region=None,types=None):
    """
    查询兴趣点的函数
    :param keywords:兴趣点名称，必要参数
    :param region:搜索区划，为城市名
    :return 返回一个JSON字符串，包含了API搜索结果的详细信息。
    """
    url = "https://restapi.amap.com/v5/place/text"
    params = {
        "key": os.getenv("GAODE_KEY"),
        "keywords": keywords,
        "region": region,
        "city_limit": 'true',
        "show_fields": "business,photos",
    }
    with httpx.Client() as client:
        response = client.get(url, params=params)
    return json.dumps(response.json())
