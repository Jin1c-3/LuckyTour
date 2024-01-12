<template>
  <v-dialog
    v-model="plan.is.showHobbyDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen">
      <div class="d-flex flex-wrap">
        <v-btn
          variant="text"
          icon="mdi-arrow-left"
          class="ml-5 mt-5 me-auto"
          @click="router.back()"
        ></v-btn>
      </div>
      <div class="text-h4 me-auto ml-5 mt-5">兴趣</div>
      <div class="text-subtitle-2 me-auto ml-5 mb-5">选择你感兴趣的标签</div>
      <div class="w-75 mx-auto">
        <v-chip-group
          multiple
          selected-class="select-chip"
          column
          filter
          v-model="plan.temp.toHobbies"
        >
          <v-chip v-for="tag in tags" variant="outlined" :key="tag">
            {{ tag }}
          </v-chip>
        </v-chip-group>
      </div>
      <div class="d-flex justify-center align-center mt-5 mb-5">
        <v-btn
          class="bg-black"
          @click="
            router.push('/plan/all');
            getTags();
          "
          width="300"
          >下一步</v-btn
        >
      </div>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";

const router = useRouter();
const plan = usePlanViewStore();
const tags = [
  "美食",
  "购物",
  "自然",
  "历史",
  "文化",
  "艺术",
  "音乐",
  "运动",
  "户外",
  "夜生活",
];
function getTags() {
  let data = [];
  plan.temp.toHobbies.forEach((tag) => {
    data.push(tags[tag]);
  });
  plan.temp.hobbies = data;
}

onActivated(() => {
  plan.is.showHobbyDialog = true;
});
onDeactivated(() => {
  plan.is.showHobbyDialog = false;
});
</script>

<style scoped>
.select-chip {
  color: rgb(255, 255, 255);
  background-color: rgb(0, 0, 0);
}
</style>
