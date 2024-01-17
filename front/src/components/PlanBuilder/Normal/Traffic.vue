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
      <v-container>
        <v-btn
          variant="text"
          icon="mdi-arrow-left"
          @click="router.back()"
        ></v-btn>
        <div class="text-h4 mt-5">交通</div>
        <div class="text-subtitle-2 mb-5">你喜欢乘坐什么交通工具出行？</div>
        <v-item-group
          selected-class="card-active"
          v-model="plan.selected.traffic"
        >
          <v-item v-slot="{ selectedClass, toggle }" v-for="item in items">
            <v-card
              :class="['d-flex align-center mt-5 mb-5', selectedClass]"
              height="100"
              @click="
                toggle();
                plan.temp.traffic = item.name;
              "
              :elevation="selectedClass ? 5 : 1"
            >
              <div>
                <div class="text-h5 ml-5">
                  {{ item.name }}
                </div>
                <div class="text-subtitle-2 ml-5">
                  {{ item.description }}
                </div>
              </div>

              <v-avatar class="mr-6 ms-auto" size="50" rounded="0">
                <v-img :src="item.url"></v-img>
              </v-avatar>
            </v-card>
          </v-item>
        </v-item-group>

        <v-btn @click="next" block color="teal-accent-4">下一步</v-btn>
      </v-container>
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
    url: new URL("@/assets/images/traffic/plane.svg", import.meta.url).href,
  },
  {
    name: "火车",
    description: "经济实惠的选择",
    url: new URL("@/assets/images/traffic/train.svg", import.meta.url).href,
  },
  {
    name: "汽车",
    description: "自由自在的旅行",
    url: new URL("@/assets/images/traffic/car.svg", import.meta.url).href,
  },
  {
    name: "轮船",
    description: "浪漫的海上之旅",
    url: new URL("@/assets/images/traffic/boat.svg", import.meta.url).href,
  },
  {
    name: "步行",
    description: "慢慢走，慢慢看",
    url: new URL("@/assets/images/traffic/walk.svg", import.meta.url).href,
  },
  {
    name: "地铁",
    description: "城市交通的首选",
    url: new URL("@/assets/images/traffic/subway.svg", import.meta.url).href,
  },
]);

/**
 * @description: 跳转到下一步
 */
function next() {
  if (plan.temp.traffic == "") return;
  router.push("/plan/hobby");
}
onActivated(() => {
  plan.is.showTrafficDialog = true;
});
onDeactivated(() => {
  plan.is.showTrafficDialog = false;
});
</script>

<style scoped>
.card-active {
  border: 2px solid #26a69a !important;
  background-color: #e0f2f1;
}
</style>
