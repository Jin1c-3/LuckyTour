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
    <div class="text-h4 me-auto ml-5 mt-5">交通</div>
    <div class="text-subtitle-2 me-auto ml-5 mb-5">
      你喜欢乘坐什么交通工具出行？
    </div>
    <v-item-group selected-class="bg-black" class="w-75 mx-auto">
      <v-item v-slot="{ selectedClass, toggle }" v-for="item in items">
        <v-card
          :class="['d-flex align-center mt-5 mb-5', selectedClass]"
          dark
          height="100"
          @click="
            plan.temp.trafficModelActive = item.name;
            toggle();
          "
          variant="outlined"
        >
          <div>
            <div class="text-h5 ml-5">
              {{ item.name }}
            </div>
            <div class="text-subtitle-2 ml-5">
              {{ item.description }}
            </div>
          </div>

          <v-icon size="50" class="ml-auto mr-5">{{ item.icon }}</v-icon>
        </v-card>
      </v-item>
    </v-item-group>
    <v-btn class="bom-btn bg-black" @click="plan.config.step++" width="300"
      >下一步</v-btn
    >
  </v-card>
</template>

<script setup>
import { ref } from "vue";
import { usePlanViewStore } from "@/stores/planView";

const plan = usePlanViewStore();
const items = ref([
  {
    name: "飞机",
    description: "快速到达目的地",
    icon: "mdi-airplane",
  },
  {
    name: "火车",
    description: "经济实惠的选择",
    icon: "mdi-train",
  },
  {
    name: "汽车",
    description: "自由自在的旅行",
    icon: "mdi-car",
  },
]);
</script>

<style scoped>
.bom-btn {
  position: absolute;
  left: 50%;
  bottom: 5px;
  transform: translate(-50%, -50%);
}
</style>
