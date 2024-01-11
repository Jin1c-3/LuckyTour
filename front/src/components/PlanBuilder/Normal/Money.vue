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
    <div class="text-h4 me-auto ml-5 mt-5">预算</div>
    <div class="text-subtitle-2 me-auto ml-5 mb-5">你想花费多少为这次旅行</div>
    <div class="text-h6 ml-5">形式</div>
    <v-item-group selected-class="bg-black" class="w-75 mx-auto">
      <v-item v-slot="{ selectedClass, toggle }" v-for="item in items">
        <v-card
          :class="['d-flex align-center mt-5 mb-5', selectedClass]"
          dark
          height="100"
          @click="
            plan.temp.costModelActive = item.name;
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
    <div class="text-h6 ml-5 mb-4">区间</div>
    <div class="w-75 mx-auto d-flex flex-wrap justify-center align-start">
      <v-slider
        v-if="plan.temp.costModelActive === '经济'"
        v-model="plan.temp.cost"
        :min="500"
        :max="2000"
        :step="100"
        ticks
        tick-size="10"
        thumb-label
      ></v-slider>
      <v-slider
        v-if="plan.temp.costModelActive === '舒适'"
        v-model="plan.temp.cost"
        :min="2000"
        :max="5000"
        :step="100"
        ticks
        tick-size="10"
        thumb-label
      ></v-slider>
      <v-slider
        v-if="plan.temp.costModelActive === '豪华'"
        v-model="plan.temp.cost"
        :min="5000"
        :max="10000"
        :step="100"
        ticks
        tick-size="10"
        thumb-label
      ></v-slider>
      <div class="text-h5">{{ plan.temp.cost }}</div>
    </div>
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
    name: "经济",
    description: "最低的价格最高的性价比",
    icon: "mdi-currency-usd",
  },
  {
    name: "舒适",
    description: "用适当的价格换取最舒适的旅行",
    icon: "mdi-cash",
  },
  {
    name: "豪华",
    description: "将旅行服务拉到最满意的程度",
    icon: "mdi-cash-multiple",
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
