<template>
  <v-dialog
    v-model="plan.is.showPeopleDialog"
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
        <div class="text-h4 mt-5">旅客</div>
        <div class="text-subtitle-2 mb-5">哪些人会加入这场旅途？</div>
        <v-item-group
          selected-class="card-active"
          v-model="plan.selected.customerType"
        >
          <v-item
            v-slot="{ selectedClass, toggle }"
            v-for="item in items"
            :key="item"
          >
            <v-card
              :class="['d-flex align-center mt-5 mb-5', selectedClass]"
              height="100"
              @click="
                toggle();
                plan.temp.customerType = item.name;
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
        <v-container
          v-if="
            plan.selected.customerType == 2 || plan.selected.customerType == 3
          "
        >
          <div class="d-flex justify-start align-center">
            <div>小孩</div>
            <v-btn
              class="ms-auto"
              @click="
                plan.selected.count.children > 0
                  ? plan.selected.count.children--
                  : 0
              "
              variant="text"
              icon="mdi-minus"
            ></v-btn>
            <span>{{ plan.selected.count.children }}</span>
            <v-btn
              @click="
                plan.selected.count.children < 10
                  ? plan.selected.count.children++
                  : 10
              "
              variant="text"
              icon="mdi-plus"
            ></v-btn>
          </div>
          <div class="d-flex justify-start align-center">
            <div>大人</div>
            <v-btn
              class="ms-auto"
              @click="
                plan.selected.count.adult > 0 ? plan.selected.count.adult-- : 0
              "
              variant="text"
              icon="mdi-minus"
            ></v-btn>
            <span>{{ plan.selected.count.adult }}</span>
            <v-btn
              @click="
                plan.selected.count.adult < 10
                  ? plan.selected.count.adult++
                  : 10
              "
              variant="text"
              icon="mdi-plus"
            ></v-btn>
          </div>
        </v-container>

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
    name: "单人",
    description: "独享一个人的旅行",
    url: new URL("@/assets/images/customerType/self.svg", import.meta.url).href,
  },
  {
    name: "情侣",
    description: "两个人的甜蜜旅程",
    url: new URL("@/assets/images/customerType/couple.svg", import.meta.url)
      .href,
  },
  {
    name: "家庭",
    description: "三个人及以上的温馨旅行",
    url: new URL("@/assets/images/customerType/family.svg", import.meta.url)
      .href,
  },
  {
    name: "朋友",
    description: "三个人及以上的有趣旅行",
    url: new URL("@/assets/images/customerType/friends.svg", import.meta.url)
      .href,
  },
]);

/**
 * @description: 跳转到下一步
 */
function next() {
  if (plan.temp.customerType == "") return;
  router.push("/plan/date");
}
onActivated(() => {
  plan.is.showPeopleDialog = true;
});
onDeactivated(() => {
  plan.is.showPeopleDialog = false;
});
</script>

<style scoped>
.card-active {
  border: 2px solid #26a69a !important;
  background-color: #e0f2f1;
}
</style>
