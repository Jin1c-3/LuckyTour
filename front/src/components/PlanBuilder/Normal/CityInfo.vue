<template>
  <v-dialog
    v-model="plan.is.showCityInfoDialog"
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
      <div class="text-h3 ml-5 mb-3 mt-5">威海</div>
      <div class="ml-5">山东省 威海市</div>
      <div class="d-flex justify-center align-center mt-5 mb-5">
        <v-btn
          class="bom-btn bg-black"
          @click="router.push('/plan/people')"
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

onActivated(() => {
  plan.is.showCityInfoDialog = true;
});
onDeactivated(() => {
  plan.is.showCityInfoDialog = false;
});
</script>

<style scoped></style>
