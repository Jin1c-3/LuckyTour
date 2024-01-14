<template>
  <div class="d-flex justify-start align-center mt-5 mb-4">
    <div class="text-h3 me-auto ml-4 title">用户</div>
  </div>
  <v-card class="mx-auto w-100" height="200" :elevation="0">
    <div
      class="d-flex justify-start align-center"
      style="height: 200px"
      v-if="user.status.login"
    >
      <v-avatar
        color="grey-darken-3"
        image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
        size="120"
        class="ml-5 mr-5"
      ></v-avatar>
      <div>
        <div class="text-h4 mb-3">Admin</div>
        <div>山东省，威海市，环翠区</div>
      </div>
    </div>
    <div
      class="d-flex flex-column justify-center align-center"
      style="height: 200px"
      v-else
    >
      <v-btn
        @click="router.push('/user/login')"
        size="x-large"
        class="mt-4"
        icon="mdi-power"
      >
      </v-btn>
      <div class="mt-3">登录后可查看更多信息</div>
    </div>
  </v-card>
  <v-list density="comfortable">
    <!-- <v-list-subheader>REPORTS</v-list-subheader> -->
    <v-list-item
      v-for="(item, i) in items"
      :key="i"
      :value="item"
      height="60"
      @click="router.push(item.to)"
    >
      <template v-slot:prepend>
        <v-icon :icon="item.icon" :size="35"></v-icon>
      </template>

      <template v-slot:append>
        <v-icon> mdi-chevron-right </v-icon>
      </template>
      <v-list-item-title v-text="item.text"></v-list-item-title>
    </v-list-item>
  </v-list>
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :is="Component" />
    </keep-alive>
  </router-view>
</template>

<script setup>
import { ref, KeepAlive } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useUserViewStore } from "@/stores/userView";

const router = useRouter();
const user = useUserViewStore();

const items = [
  { text: "我的账户", icon: "mdi-clock", to: "/user/update" },
  { text: "帮助", icon: "mdi-account" },
  { text: "设置", icon: "mdi-cog" },
];
</script>

<style scoped>
.title {
  color: var(--firstLevel-head-color);
}
</style>
