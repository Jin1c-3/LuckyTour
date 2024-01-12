<template>
  <v-dialog
    v-model="plan.is.showDateDialog"
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

      <div class="text-h4 me-auto ml-5 mt-5">日期</div>
      <div class="text-subtitle-2 me-auto ml-5 mb-5">什么时候开始旅程？</div>
      <v-container>
        <div class="text-h5 ml-5">起始日期</div>
        <v-date-picker
          class="mx-auto"
          show-adjacent-months
          v-model="plan.temp.beginDate"
        ></v-date-picker>
        <div class="text-h5 ml-5">结束日期</div>
        <v-date-picker
          class="mx-auto"
          show-adjacent-months
          v-model="plan.temp.endDate"
          :allowed-dates="allowedDates"
        ></v-date-picker>
      </v-container>

      <div class="d-flex justify-center align-center mt-5 mb-5">
        <v-btn
          class="bg-black"
          @click="
            router.push('/plan/money');
            changeTime();
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

function allowedDates(date) {
  if (plan.temp.beginDate != null) {
    return date.getTime() >= plan.temp.beginDate.getTime();
  }
}

onActivated(() => {
  plan.is.showDateDialog = true;
});
onDeactivated(() => {
  plan.is.showDateDialog = false;
});
</script>

<style scoped></style>
