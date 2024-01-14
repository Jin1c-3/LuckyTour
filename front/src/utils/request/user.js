import service from "../request";
import { useUserViewStore } from "@/stores/userView";

const user = useUserViewStore();

/**
 * @description 用户登录
 * @param {object} data 用户登录输入的数据
 * @returns {object} result 返回结果
 */
async function login(data) {
  const response = await service.post("/auth/login", data);
  const result = response.data;
  return result;
}

/**
 * @description 获取用户信息
 * @returns {object} result 返回结果
 */
async function getUserInfo() {
  const response = await service.get("/user/getinfo");
  const result = response.data;
  return result;
}

/**
 * @description 用户登出
 * @param {string} data 用户登出通知
 */
async function logout(data) {
  const response = await service.post("/user/login", data);
  const result = response.data;
  //TODO 对返回结果进行处理
}

/**
 * @description 用户注册时获取验证码
 * @param {object} params 用户注册输入的数据
 */
async function getValidateCode(params) {
  const response = await service.get("/auth/getcode", { params });
  const result = response.data;
  user.loginOrRegister.validateCode = result.data;
}

/**
 * @description 创建新用户
 * @param {object} data 用户注册相关信息
 * @returns {object} result 返回结果
 */
async function createUser(params) {
  const response = await service.get("/user/new", { params });
  const result = response.data;
  return result;
}

/**
 * @description 用户修改信息
 * @param {object} data 用户修改信息输入的数据
 * @returns {object} result 返回结果
 */
async function updateUserInfo(params, data) {
  const response = await service({
    url: "/user/update",
    method: "post",
    headers: { "Content-Type": "multipart/form-data" },
    params,
    data,
  });
  const result = response.data;
  return result;
}

export {
  login,
  getUserInfo,
  logout,
  getValidateCode,
  createUser,
  updateUserInfo,
};
