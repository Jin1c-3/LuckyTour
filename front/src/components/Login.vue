<template>
  <v-dialog
    v-model="user.is.showLoginDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen">
      <v-container>
        <div class="text-h2 text-center mt-16 text-teal-accent-4">云 栖</div>
        <v-text-field
          v-model="user.loginOrRegister.emailOrPhone"
          label="邮箱/手机号"
          variant="outlined"
          class="mt-5 input"
          clearable
          :rules="[isEmailOrPhone]"
          base-color="teal-lighten-2"
          color="teal-darken-1"
        ></v-text-field>
        <v-text-field
          v-model="user.loginOrRegister.password"
          v-if="!mode"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          :type="visible ? 'text' : 'password'"
          label="密码"
          variant="outlined"
          class="mt-5 input"
          clearable
          :rules="[requirePassword]"
          @click:append-inner="visible = !visible"
          base-color="teal-lighten-2"
          color="teal-darken-1"
        ></v-text-field>
        <div class="d-flex justify-end align-center">
          <v-btn
            @click="
              mode = !mode;
              loading = false;
            "
            variant="plain"
            :ripple="false"
            append-icon="mdi-refresh"
            color="teal-accent-3"
          >
            邮箱/短信验证码登录
          </v-btn>
        </div>
        <v-btn
          @click="Login"
          block
          size="large"
          :loading="loading"
          class="mt-4"
          color="teal-accent-4"
        >
          {{ mode ? "获取验证码" : "登录" }}
        </v-btn>
        <div class="d-flex justify-center align-center my-2">
          <v-btn
            @click="router.replace('/user/register')"
            append-icon="mdi-chevron-right"
            variant="plain"
            :ripple="false"
            color="teal-accent-3"
          >
            还未注册？立即注册
          </v-btn>
        </div>
      </v-container>
    </v-card>
  </v-dialog>

  <v-snackbar
    v-model="snackbar"
    :timeout="2000"
    color="teal-accent-4"
    rounded="pill"
  >
    {{ content }}
  </v-snackbar>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useUserViewStore } from "@/stores/userView";
import { login, getValidateCode, getUserInfo } from "@/utils/request/user";

const router = useRouter();
const user = useUserViewStore();

let loading = ref(false);
let mode = ref(false);
let visible = ref(false);
let snackbar = ref(false);
let content = ref("");

/**
 *@description 发送登录请求
 */
async function Login() {
  if (mode.value) {
    user.loginOrRegister.type = "login";
    router.push("/user/validateCode");
    getValidateCode({
      emailOrPhone: user.loginOrRegister.emailOrPhone,
    });
    mode.value = false;
  } else {
    if (validate() == true) {
      loading.value = true;
      const result = await login({
        emailOrPhone: user.loginOrRegister.emailOrPhone,
        password: user.loginOrRegister.password,
        jrid: user.info.jrid,
        rememberMe: user.loginOrRegister.rememberMe,
      });
      loading.value = false;
      content.value = result.message;
      snackbar.value = true;
      if (result.code == 200) {
        user.status.login = true;
        localStorage.setItem("token", result.data.token);
        const userInfo = await getUserInfo();
        Object.assign(user.info, userInfo.data);
        setTimeout(() => {
          router.back();
        }, 2000);
      }
    }
  }
}

function validate() {
  let count = 0;
  count += isEmailOrPhone() == true ? 0 : 1;
  count += requirePassword() == true ? 0 : 1;
  return count == 0 ? true : false;
}

function isEmailOrPhone() {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const phoneRegex = /^1[3456789]\d{9}$/;

  if (
    emailRegex.test(user.loginOrRegister.emailOrPhone) ||
    phoneRegex.test(user.loginOrRegister.emailOrPhone)
  ) {
    return true;
  } else {
    return "请输入正确的邮箱或手机号";
  }
}

function requirePassword() {
  if (user.loginOrRegister.password == "") {
    return "请输入密码";
  } else {
    return true;
  }
}

onActivated(() => {
  user.is.showLoginDialog = true;
});
onDeactivated(() => {
  user.is.showLoginDialog = false;
});
</script>

<style scoped></style>
