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
    <div class="text-h4 me-auto ml-5 mt-5">城市</div>
    <div class="text-subtitle-2 me-auto ml-5 mb-5">你想去哪个城市？</div>
    <v-autocomplete
      clearable
      label="城市"
      variant="outlined"
      class="w-75 mx-auto"
      prepend-inner-icon="mdi-magnify"
      theme="light"
    ></v-autocomplete>
    <v-container>
      <v-card
        v-ripple
        variant="outlined"
        class="mx-auto mb-5"
        @click="
          plan.temp.city = item;
          plan.is.showCitySheet = true;
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

    <v-bottom-sheet v-model="plan.is.showCitySheet">
      <v-card height="800">
        <div class="text-h3 ml-5 mb-3 mt-5">威海</div>
        <div class="ml-5">山东省 威海市</div>
        <v-btn
          class="bom-btn bg-black"
          @click="
            plan.config.step++;
            plan.is.showCitySheet = false;
          "
          width="300"
          >下一步</v-btn
        >
      </v-card>
    </v-bottom-sheet>
  </v-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { usePlanViewStore } from "@/stores/planView";
// import { useBackStore } from "@/stores/back";

const plan = usePlanViewStore();
// const back = useBackStore();
const items = ref([
  {
    province: "山东",
    city: "威海",
  },
]);

// onMounted(() => {
//   back.pushHistory();
//   back.addPopstateListener(() => {
//     console.log("listened");
//     plan.config.step--;
//   });
// });

// onUnmounted(() => {
//   console.log("unmounted");
//   back.removePopstateListener(() => {
//     console.log("remove");
//   });
// });
</script>

<style scoped>
.bom-btn {
  position: absolute;
  left: 50%;
  bottom: 5px;
  transform: translate(-50%, -50%);
}
</style>
