<template>
  <v-dialog
    v-model="user.is.showUpdateDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen"> </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useUserViewStore } from "@/stores/userView";
import { updateUserInfo } from "@/utils/request/user";

const router = useRouter();
const user = useUserViewStore();

const items = [
  { text: "我的账户", icon: "mdi-clock" },
  { text: "帮助", icon: "mdi-account" },
  { text: "设置", icon: "mdi-cog" },
];

function send() {
  let data = new FormData();
  data.append("avatarPic", "");
  updateUserInfo(user.temp, data);
}

onActivated(() => {
  user.is.showUpdateDialog = true;
});
onDeactivated(() => {
  user.is.showUpdateDialog = false;
});
</script>

<style scoped></style>
