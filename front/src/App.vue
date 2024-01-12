<template>
  <v-app>
    <RouterView />
    <div style="width: 100%; height: 50px"></div>
    <v-bottom-navigation mode="shift">
      <v-btn @click="router.replace('/')" max-width="50px">
        <v-icon icon="mdi-home" />
        <span>首页</span>
      </v-btn>
      <v-btn @click="router.replace('/discover')">
        <v-icon icon="mdi-navigation-variant" />
        <span>发现</span>
      </v-btn>
      <v-btn @click="router.replace('/plan')">
        <v-icon icon="mdi-map-plus" />
        <span>计划</span> </v-btn
      ><v-btn @click="router.replace('/user')">
        <v-icon icon="mdi-account-circle" />
        <span>用户</span>
      </v-btn>
    </v-bottom-navigation>
    <v-snackbar
      v-model="snackbar"
      :timeout="2000"
      color="blue-grey"
      rounded="pill"
      class="snackbar"
    >
      再按一次返回键退出应用
    </v-snackbar>
  </v-app>
</template>

<script setup>
import { RouterLink, RouterView, useRouter } from "vue-router";
import { ref, onMounted } from "vue";

const router = useRouter();
const root = ["plan", "discover", "user", "home"];
let active = ref(0);
let value = ref(1);
let snackbar = ref(false);
let lastTime = 0;

onMounted(() => {
  // router.replace("/home");
  document.addEventListener("plusready", () => {
    plus.key.addEventListener("backbutton", () => {
      let flag = false;
      root.forEach((item) => {
        if (router.currentRoute.value.name == item) {
          flag = true;
          const now = new Date().getTime();
          if (now - lastTime < 2000) {
            snackbar.value = false;
            plus.runtime.quit();
          }
          snackbar.value = true;
          lastTime = now;
        }
      }, false);
      if (flag) return;
      window.history.go(-1);
    });
  });
});
</script>

<style>
:root {
  --firstLevel-head-color: #00cfda;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  background-color: white;
}
.snackbar {
  transform: translateY(-70px) !important;
}
</style>
