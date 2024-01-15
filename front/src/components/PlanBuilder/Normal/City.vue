<template>
  <v-dialog
    v-model="plan.is.showCityDialog"
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
      <div class="text-h4 me-auto ml-5 mt-5">城市</div>
      <div class="text-subtitle-2 me-auto ml-5 mb-5">你想去哪个城市？</div>
      <v-container>
        <v-autocomplete
          clearable
          label="城市"
          variant="outlined"
          prepend-inner-icon="mdi-magnify"
          theme="light"
        ></v-autocomplete>
        <v-card
          v-ripple
          variant="outlined"
          class="mx-auto mb-5"
          @click="
            plan.temp.city = item;
            router.push('/plan/cityInfo');
          "
          v-for="item in items"
        >
          <div class="d-flex flex-wrap justify-start align-center">
            <v-avatar class="ma-3" size="90" rounded="0">
              <v-img
                src="https://cdn.vuetifyjs.com/images/cards/halcyon.png"
              ></v-img>
            </v-avatar>
            <div>
              <div class="text-h4 mb-3">威海</div>
              <div>山东省 威海市</div>
            </div>
          </div>
        </v-card>
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
    province: "山东",
    city: "威海",
  },
]);

onActivated(() => {
  plan.is.showCityDialog = true;
});
onDeactivated(() => {
  plan.is.showCityDialog = false;
});
</script>

<style scoped>
.bom-btn {
  position: absolute;
  left: 50%;
  bottom: 5px;
  transform: translate(-50%, -50%);
}
</style>
