<template>
  <v-dialog
    v-model="user.is.showValidateDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen" :loading="loading">
      <v-container>
        <div class="text-h4 text-center mt-16">验证码已发送</div>
        <div class="text-subtitle-1 text-center h-25 mt-16 mx-7">
          我们已向你提供的邮箱或电话号码发送了一串验证数字，请及时接收并在两分钟内填写在下方
        </div>
        <v-otp-input
          v-model="otp"
          :disabled="disabled"
          :error="error"
          @click="error = false"
        ></v-otp-input>
        <div class="text-subtitle-1 text-center">
          没有收到验证码？
          <a href="#" @click.prevent="resend">重发</a>
        </div>
      </v-container>
    </v-card>

    <v-snackbar
      v-model="snackbar"
      :timeout="2000"
      class="snackbar"
      color="blue-grey"
      rounded="pill"
    >
      {{ content }}
    </v-snackbar>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated, watch } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useUserViewStore } from "@/stores/userView";
import { getValidateCode, createUser } from "@/utils/request/user";

const router = useRouter();
const user = useUserViewStore();

let otp = ref("");
let loading = ref(false);
let disabled = ref(false);
let error = ref(false);
let content = ref("");
let snackbar = ref(false);

/**
 *@description 发送请求
 */
function send() {
  switch (user.loginOrRegister.type) {
    case "register":
      thenRegister();
      break;
    case "login":
      thenLogin();
      break;
  }
}

/**
 *@description 下一步注册
 */
async function thenRegister() {
  const result = await createUser({
    emailOrPhone: user.loginOrRegister.emailOrPhone,
  });
  loading.value = false;
  content.value = result.message;
  snackbar.value = true;
  if (result.code == 200) {
    setTimeout(() => {
      window.history.go(-(window.history.length - 1));
      disabled.value = false;
      content.value = "";
      otp.value = "";
    }, 2000);
    content.value = result.message;
  } else {
    setTimeout(() => {
      router.back();
      disabled.value = false;
      content.value = "";
      otp.value = "";
    }, 2000);
  }
}

/**
 *@description 下一步登录
 */
async function thenLogin() {
  const result = await login({
    emailOrPhone: user.loginOrRegister.emailOrPhone,
    jrid: user.info.jrid,
    rememberMe: user.loginOrRegister.rememberMe,
  });
}

/**
 *@description 重发验证码
 */
function resend() {
  getValidateCode({
    emailOrPhone: user.loginOrRegister.emailOrPhone,
  });
}

watch(otp, (newVal) => {
  if (newVal.length === 6) {
    loading.value = true;
    disabled.value = true;
    if (otp.value == user.loginOrRegister.validateCode) {
      send();
    } else {
      error.value = true;
      loading.value = false;
      disabled.value = false;
    }
  }
});

onActivated(() => {
  user.is.showValidateDialog = true;
});
onDeactivated(() => {
  user.is.showValidateDialog = false;
});
</script>

<style scoped></style>
