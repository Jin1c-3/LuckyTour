import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";

export const useUserViewStore = defineStore("userView", () => {
  let is = reactive({
    showLoginDialog: false,
    showRegisterDialog: false,
    showValidateDialog: false,
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
  });
  let loginOrRegister = reactive({
    type: "",
    emailOrPhone: "",
    password: "",
    validateCode: "",
  });
  return {
    is,
    info,
    loginOrRegister,
  };
});
