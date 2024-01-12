import service from "../request";
import { useUserViewStore } from "@/stores/userView";

const user = useUserViewStore();

/**
 * @description 用户登录
 * @param {object} data 用户登录输入的数据
 */
async function login(data) {
  const response = await service.post("/user/login", data);
  const result = response.data;
  //TODO 对返回结果进行处理
}

/**
 * @description 获取用户信息
 */
async function getUserInfo() {
  const response = await service.post("/user/getInfo", data);
  const result = response.data;
  //TODO 对返回结果进行处理
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
 */
async function createUser(data) {
  const response = await service.post("/user/new", data);
  const result = response.data;
  //TODO 对返回结果进行处理
}

/**
 * @description 用户修改信息
 * @param {object} data 用户修改信息输入的数据
 */
async function updateUserInfo(data) {
  const response = await service.post("/user/update", data);
  const result = response.data;
  //TODO 对返回结果进行处理
}

export {
  login,
  getUserInfo,
  logout,
  getValidateCode,
  createUser,
  updateUserInfo,
};
