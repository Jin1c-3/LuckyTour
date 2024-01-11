<template>
  <v-app>
    <RouterView />
    <div style="width: 100%; height: 50px"></div>
    <v-bottom-navigation mode="shift">
      <v-btn @click="router.push('/')" max-width="50px">
        <v-icon icon="mdi-home" />
        <span>首页</span>
      </v-btn>
      <v-btn @click="router.push('/discover')">
        <v-icon icon="mdi-navigation-variant" />
        <span>发现</span>
      </v-btn>
      <v-btn @click="router.push('/plan')">
        <v-icon icon="mdi-map-plus" />
        <span>计划</span> </v-btn
      ><v-btn @click="router.push('/user')">
        <v-icon icon="mdi-account-circle" />
        <span>用户</span>
      </v-btn>
    </v-bottom-navigation>
  </v-app>
</template>

<script setup>
import { RouterLink, RouterView, useRouter } from "vue-router";
import { ref, onMounted } from "vue";

const router = useRouter();
let active = ref(0);
let value = ref(1);

onMounted(() => {
  router.push("/");
});
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  background-color: white;
}
</style>
