import { service_, service } from "../request";

/**
 * @description GPT聊天
 * @param {object} data 用户询问的信息
 * @returns {object} result 返回结果
 */
async function chat(data) {
  const response = await service_({
    method: "post",
    url: "/generateFreeModeTravelItinerary",
    headers: {
      "Content-Type": "application/json",
    },
    data,
  });
  const result = response.data;
  return result;
}

/**
 * @description 生成行程
 * @param {object} data  用户询问的信息
 * @returns {object} result 返回结果
 */
async function generatePlan(data) {
  const response = await service_.post("/generatePlanner", data);
  const result = response.data;
  return result;
}

/**
 * @description 获取城市信息列表
 * @param {object} params 包含城市名的字符串
 * @returns {object} result 返回结果
 */
async function getCities(params) {
  const response = await service.get("/data/getCityDescription", { params });
  const result = response.data;
  return result;
}

export { chat, generatePlan, getCities };
