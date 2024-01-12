<template>
  <v-dialog
    v-model="plan.is.showTrafficDialog"
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
      <div class="d-flex justify-center align-center mt-5 mb-5">
        <v-btn class="bg-black" @click="router.push('/plan/hobby')" width="300"
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

onActivated(() => {
  plan.is.showTrafficDialog = true;
});
onDeactivated(() => {
  plan.is.showTrafficDialog = false;
});
</script>

<style scoped></style>
