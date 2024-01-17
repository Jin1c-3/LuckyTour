<template>
  <v-dialog
    v-model="plan.is.showMoneyDialog"
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
        <div class="text-h4 mt-5">预算</div>
        <div class="text-subtitle-2 mb-5">你想花费多少为这次旅行</div>
        <v-item-group
          selected-class="card-active"
          v-model="plan.selected.budget"
        >
          <v-item v-slot="{ selectedClass, toggle }" v-for="item in items">
            <v-card
              :class="['d-flex align-center mt-5 mb-5', selectedClass]"
              height="100"
              @click="
                toggle();
                plan.temp.budget = item.name;
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
    name: "经济",
    description: "最低的价格最高的性价比",
    url: new URL("@/assets/images/budget/cheap.svg", import.meta.url).href,
  },
  {
    name: "舒适",
    description: "用适当的价格换取最舒适的旅行",
    url: new URL("@/assets/images/budget/moderate.svg", import.meta.url).href,
  },
  {
    name: "豪华",
    description: "将旅行服务拉到最满意的程度",
    url: new URL("@/assets/images/budget/luxury.svg", import.meta.url).href,
  },
]);

/**
 * @description: 跳转到下一步
 */
function next() {
  if (plan.temp.budget == "") return;
  router.push("/plan/traffic");
}

onActivated(() => {
  plan.is.showMoneyDialog = true;
});
onDeactivated(() => {
  plan.is.showMoneyDialog = false;
});
</script>

<style scoped>
.card-active {
  border: 2px solid #26a69a !important;
  background-color: #e0f2f1;
}
</style>
