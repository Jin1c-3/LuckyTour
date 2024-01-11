<template>
  <v-card class="h-screen">
    <div class="d-flex flex-wrap">
      <v-btn
        variant="text"
        icon="mdi-arrow-left"
        class="ml-5 mt-5 me-auto"
        @click="plan.config.step--"
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

    <v-btn
      class="bom-btn bg-black"
      @click="
        plan.config.step++;
        getTags();
      "
      width="300"
      >下一步</v-btn
    >
  </v-card>
</template>

<script setup>
import { ref } from "vue";
import { usePlanViewStore } from "@/stores/planView";

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
</script>

<style scoped>
.bom-btn {
  position: absolute;
  left: 50%;
  bottom: 5px;
  transform: translate(-50%, -50%);
}
.select-chip {
  color: rgb(255, 255, 255);
  background-color: rgb(0, 0, 0);
}
</style>
