<template>
  <v-dialog
    v-model="user.is.showRegisterDialog"
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
          :rules="[isEmailOrPhone]"
          label="邮箱/手机号"
          variant="outlined"
          class="mt-5"
          clearable
          base-color="teal-lighten-2"
          color="teal-darken-1"
        ></v-text-field>

        <v-btn
          @click="Register"
          block
          size="large"
          class="mt-4"
          color="teal-accent-4"
        >
          注册
        </v-btn>
        <div class="d-flex justify-center align-center my-2">
          <v-btn
            @click="router.replace('/user/login')"
            append-icon="mdi-chevron-right"
            variant="plain"
            :ripple="false"
            color="teal-accent-3"
          >
            已有账号？立即登录
          </v-btn>
        </div>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useUserViewStore } from "@/stores/userView";
import { getValidateCode } from "@/utils/request/user";

const router = useRouter();
const user = useUserViewStore();

// TODO 发送请求
function Register() {
  if (validate() == true) {
    user.loginOrRegister.type = "register";
    router.push("/user/validateCode");
    getValidateCode({
      emailOrPhone: user.loginOrRegister.emailOrPhone,
    });
  }
}

function validate() {
  return isEmailOrPhone();
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

onActivated(() => {
  user.is.showRegisterDialog = true;
});
onDeactivated(() => {
  user.is.showRegisterDialog = false;
});
</script>

<style scoped></style>
