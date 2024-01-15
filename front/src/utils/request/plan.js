import { service_ } from "../request";

/**
 * @description GPT聊天
 * @param {object} data 用户询问的信息
 * @returns {object} result 返回结果
 */
async function chat(data) {
  const response = await service_({
    method: "post",
    url: "/chat",
    headers: {
      "Content-Type": "application/json",
    },
    data,
  });
  const result = response.data;
  return result;
}

export { chat };
