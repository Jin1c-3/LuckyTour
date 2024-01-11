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
    <div class="full-scroll">
      <div class="text-h4 me-auto ml-5 mt-5">日期</div>
      <div class="text-subtitle-2 me-auto ml-5 mb-5">什么时候开始旅程？</div>
      <div class="text-h5 ml-5">起始日期</div>
      <v-date-picker
        class="mx-auto"
        show-adjacent-months
        width="400"
        v-model="plan.temp.beginDate"
      ></v-date-picker>
      <div class="text-h5 ml-5">结束日期</div>
      <v-date-picker
        class="mx-auto"
        show-adjacent-months
        width="400"
        v-model="plan.temp.endDate"
        :allowed-dates="allowedDates"
      ></v-date-picker>
    </div>
    <v-btn
      class="bom-btn bg-black"
      @click="
        plan.config.step++;
        changTime();
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

function changTime() {
  let year = plan.temp.beginDate.getFullYear();
  let month = plan.temp.beginDate.getMonth() + 1;
  let day = plan.temp.beginDate.getDate();
  plan.temp.beginDate = year + "-" + month + "-" + day;
  year = plan.temp.endDate.getFullYear();
  month = plan.temp.endDate.getMonth() + 1;
  day = plan.temp.endDate.getDate();
  plan.temp.endDate = year + "-" + month + "-" + day;
}
function allowedDates(date) {
  if (plan.temp.beginDate != null) {
    return date.getTime() >= plan.temp.beginDate.getTime();
  }
}
</script>

<style scoped>
.bom-btn {
  position: absolute;
  left: 50%;
  bottom: 5px;
  transform: translate(-50%, -50%);
}
.full-scroll {
  overflow-y: scroll;
  height: 85%;
}
</style>
