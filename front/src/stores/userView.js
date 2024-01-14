import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";

export const useUserViewStore = defineStore("userView", () => {
  let is = reactive({
    showLoginDialog: false,
    showRegisterDialog: false,
    showValidateDialog: false,
    showUpdateDialog: false,
    showUserInfoDialog: false,
  });
  let status = reactive({
    login: false,
  });
  let info = reactive({
    id: "",
    nickname: "",
    password: "",
    phone: "",
    email: "",
    birthday: "",
    sex: "",
    avatar: "",
    jrid: "",
    year: "",
    month: "",
    day: "",
  });
  let temp = reactive({
    id: "",
    nickname: "",
    password: "",
    phone: "",
    email: "",
    birthday: "",
    sex: "",
  });
  let loginOrRegister = reactive({
    type: "",
    emailOrPhone: "",
    password: "",
    validateCode: "",
    rememberMe: false,
  });
  return {
    is,
    temp,
    status,
    info,
    loginOrRegister,
  };
});
