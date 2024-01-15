import json
import os

import langchain.memory.entity
from langchain.chat_models import AzureChatOpenAI
from flask import Flask, request
import httpx
from dotenv import load_dotenv
from langchain.memory import ConversationSummaryBufferMemory, ConversationBufferWindowMemory
from langchain.prompts.prompt import PromptTemplate
from langchain import LLMChain
from tools import searchTravelPol
import myjson
from langchain.prompts import (
    ChatPromptTemplate,
    MessagesPlaceholder,
    SystemMessagePromptTemplate,
    HumanMessagePromptTemplate,
)

app = Flask(__name__)
load_dotenv(dotenv_path='./config.env')
# 初始化AzureOpenAI和其他相关组件
llm = AzureChatOpenAI(
    api_version="2023-12-01-preview",
    azure_endpoint=os.getenv("AZURE_ENDPOINT"),
    api_key=os.getenv("AZURE_OPENAI_KEY"),
    azure_deployment='test',
    http_client=httpx.Client(
        proxies="http://localhost:7890",
        transport=httpx.HTTPTransport(local_address="0.0.0.0"),
        verify=False
    )
)


def getGptResponse(data):
    """
    获取GPT生成的旅行建议
    :param data: 包含旅行信息的字典
    :return: GPT生成的旅行建议
    """
    # 系统指令
    systemCommands = """"作为旅行规划助手，''你的任务是接收关于旅客类型、预算水平、旅行日期和兴趣的信息，并按json格式回答。并按时间顺序规划景点(" 
    "不需要规划高铁站飞机站等地点）。请一定注意点之间不要离得太远''请你规划的时候查询该地区在网上热门的景点," "并选取一个作为出发点。尽量避免音译错误同时你要关注的是整体景点而非其内部的具体地点,例如：一定不要推荐山东大学(" 
    "威海校区)-孔子像这种内部景点，而是要推荐山东大学(威海校区）这种整体景点。最关键的更新是，你现在只需以特定的JSON格式回复规划好的行程，而不提供规划过程的详细描述。你的回复应严格遵循以下json格式：'\n\n" 
    "{{\"YYYY-MM-DD\":[\n{{\"name\": \"景点A\",\"兴趣点类型\":\"景点\"}},\n{{\"name\": 
    \"景点B\",\"兴趣点类型\":\"景点\"}},\n " "{{\"name\": \"景点C\",\"兴趣点类型\":\"景点\"}}],\n                        
     " "\"YYYY-MM-DD\":[\n {{\"name\": \"景点A\",\"兴趣点类型\":\"景点\"}},\n {{\"name\": 
     \"景点B\",\"兴趣点类型\":\"景点\"}},\n" "{{\"name\": \"景点C\",\"兴趣点类型\":\"景点\"}}],\n                       
      " "\"// 更多日期 \"]}}"""

    # 从输入数据中获取所需信息
    selectedTraveler = data.get('destination')
    startDate = data.get('startDate')
    endDate = data.get('endDate')
    travelDestination = data.get('customerType')
    selectedInterests = ', '.join(data.get('tag', []))
    selectedBudget = data.get('budget')
    # 构建人类输入字符串
    humanInput = f"旅客类型:{selectedTraveler}, 旅行时间:{startDate} - {endDate}, 旅行城市:{travelDestination}, 旅客兴趣:{selectedInterests}, 预算水平:{selectedBudget}"
    memory = ConversationBufferWindowMemory(llm=llm, k=2, memory_key="chat_history",return_messages=True)
    # 设置GPT生成的prompt模板
    prompt = ChatPromptTemplate(
        messages=[
            SystemMessagePromptTemplate.from_template(systemCommands),
            # The `variable_name` here is what must align with memory
            MessagesPlaceholder(variable_name="chat_history"),
            HumanMessagePromptTemplate.from_template("{humanInput}")
        ]
    )

    # 初始化LLMChain
    llmChain = LLMChain(
        llm=llm,
        prompt=prompt,
        verbose=True,
        memory=memory
    )
    # llmChain.predict(humanInput=humanInput)
    #为节省api费用使用的测试数据
    memory.chat_memory.add_user_message(
        f"旅客类型:{selectedTraveler}, 旅行时间:{startDate}--{endDate}, "
        f"旅行城市:{travelDestination}, 旅客兴趣:{selectedInterests}, 预算水平:{selectedBudget}"
    )
    memory.chat_memory.add_ai_message('''
    {
        "2024-01-21": [
            {"name": "泸州长江大桥", "兴趣点类型": "景点"},
            {"name": "四川省泸州市泸州老窖集团", "兴趣点类型": "景点"},
            {"name": "蜀南竹海", "兴趣点类型": "景点"}
        ],
        "2024-01-22": [
            {"name": "泸州白塔公园", "兴趣点类型": "景点"},
            {"name": "江阳区龙马潭", "兴趣点类型": "景点"},
            {"name": "泸州古蔺沱江戏水公园", "兴趣点类型": "景点"}
        ],
        "2024-01-23": [
            {"name": "泸州国窖酒文化城", "兴趣点类型": "景点"},
            {"name": "泸州雒城历史文化街区", "兴趣点类型": "景点"},
            {"name": "泸州市博物馆", "兴趣点类型": "景点"}
        ]
    }
    ''')
    print(memory.chat_memory.messages[-1].content)
    return memory.chat_memory.messages[-1].content


@app.route('/generatePlanner', methods=['POST'])
def generatePlanner():
    """
    处理POST请求，生成旅行计划。
    :return: 包含生成的旅行计划的JSON响应
    """
    # 从请求中获取JSON数据
    data = request.get_json()
    # 调用getGptResponse函数获取GPT生成的旅行建议
    gptResponse = getGptResponse(data)
    # 获取旅行城市信息
    city = data.get('travelPurpose')
    # 调用getMatpData函数获取旅行兴趣点的详细信息
    result = getMatpData(gptResponse, city)

    if "error" in result:
        return myjson.makeReponse(code="500", message="失败", data=result)
    else:
        return myjson.makeReponse(code="200", message="成功", data=result)


def getMatpData(data, city):
    """
    使用高德api获取旅行兴趣点的详细信息，并组织成新的数据结构
    :param data: 包含旅行兴趣点信息的JSON字符串
    :param city: 旅行的城市
    :return: 包含详细信息的新数据结构
    """
    # 将JSON字符串解析为Python字典
    try:
        # 尝试解析 JSON 字符串
        data = json.loads(data)
    except json.decoder.JSONDecodeError as e:
        error_message = f"Error decoding JSON: {e}"
        print(error_message)
        return {"error": error_message}
    # 存储新的响应结构
    new_response = {}
    # 遍历日期和兴趣点信息
    for date, places in data.items():
        # 初始化每个日期的新结构
        new_response[date] = []
        # 遍历每个兴趣点信息
        for place in places:
            # 提取兴趣点的名称和类型
            name = place['name']
            posType = place['兴趣点类型']
            # 调用高德api获取兴趣点的详细信息
            detailedInfo = searchTravelPol(name, city, posType)
            # 解析详细信息的JSON字符串
            detailedInfo = json.loads(detailedInfo)['pois'][0]
            # 需要过滤的字段
            fieldsToRemove = ['parent', 'distance', 'id']
            # 过滤详细信息中的特定字段
            for field in fieldsToRemove:
                detailedInfo.pop(field, None)
            # 将详细信息添加到新结构中
            new_response[date].append(detailedInfo)
    return new_response


@app.route('/chat', methods=['POST'])
def chat():
    # 提示词模板
    template = """"你是一个聊天助手小明"
    {chat_history}
    Human: {human_input}
    Chatbot:"""
    memory = ConversationSummaryBufferMemory(llm=llm, max_token_limit=300, memory_key="chat_history")
    # 从请求中获取JSON数据
    data = request.get_data()
    data = json.loads(data)
    data = data['ask']
    print(data)
    humanInput = data
    # 设置GPT生成的prompt模板
    prompt = PromptTemplate(
        input_variables=["chat_history", "human_input"],
        template=template
    )
    llmChain = LLMChain(
        llm=llm,
        prompt=prompt,
        verbose=True,
        memory=memory
    )
    llmChain.predict(human_input=humanInput)
    if "error" in data:
        return myjson.makeReponse(code="500", message="失败", data=memory.chat_memory.messages[-1].content)
    else:
        return myjson.makeReponse(code="200", message="成功", data=memory.chat_memory.messages[-1].content)


# @app.route('/generateFreeModeTravelItinerary', methods=['POST'])
# def generateFreeModeTravelItinerary():
#     langchain.memory.entity.RedisEntityStore()


if __name__ == '__main__':
    app.run(debug=True, host='172.29.20.72')
