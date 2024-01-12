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
        <div class="text-h2 text-center mt-16">云 栖</div>
        <v-text-field
          label="邮箱/手机号"
          variant="outlined"
          class="mt-5"
          clearable
        ></v-text-field>
        <v-text-field
          v-if="!mode"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          :type="visible ? 'text' : 'password'"
          label="密码"
          variant="outlined"
          class="mt-5"
          clearable
          @click:append-inner="visible = !visible"
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
        >
          {{ mode ? "获取验证码" : "登录" }}
        </v-btn>
        <div class="d-flex justify-center align-center my-2">
          <v-btn
            @click="router.replace('/user/register')"
            append-icon="mdi-chevron-right"
            variant="plain"
            :ripple="false"
          >
            还未注册？立即注册
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

const router = useRouter();
const user = useUserViewStore();

let loading = ref(false);
let mode = ref(false);
let visible = ref(false);

function Login() {
  if (mode.value) {
    router.push("/user/validateCode");
  } else {
    loading.value = true;
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
