<template>
  <v-dialog
    v-model="user.is.showUpdateDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen">
      <v-list density="comfortable">
        <!-- <v-list-subheader>REPORTS</v-list-subheader> -->
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :value="item"
          height="60"
        >
          <template v-slot:append>
            <v-icon> mdi-chevron-right </v-icon>
          </template>
          <v-list-item-title v-text="item.text"></v-list-item-title>
        </v-list-item>
      </v-list>
      <v-btn @click="send" block size="large" class="mt-4"> 发送 </v-btn>
    </v-card>
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
