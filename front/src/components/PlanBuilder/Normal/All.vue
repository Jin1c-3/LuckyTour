<template>
  <v-dialog
    v-model="plan.is.showAllDialog"
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
        <div class="text-h4 mt-5">预览</div>
        <div class="text-subtitle-2 mb-5">确认创建信息是否正确</div>
        <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
          <v-icon
            size="30"
            icon="mdi-map-marker-radius-outline"
            color="teal-accent-4"
          />
          <div class="text-h6 ml-1 mr-5">城市</div>
        </div>
        <v-container>
          <div class="d-flex flex-wrap justify-start align-center">
            <v-avatar class="ma-3" size="50" rounded="0">
              <v-img :src="plan.selected.activeCityInfo.photos[0]"></v-img>
            </v-avatar>
            <div>
              <div class="text-h5">{{ plan.selected.activeCityInfo.city }}</div>
              <div>
                {{ plan.selected.activeCityInfo.province }},{{
                  plan.selected.activeCityInfo.city
                }}
              </div>
            </div>
          </div>
        </v-container>
        <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
          <v-icon size="30" icon="mdi-account" color="teal-accent-4" />
          <div class="text-h6 ml-1 mr-5">旅客</div>
        </div>
        <v-container>
          <div class="mb-3">类型</div>
          <div class="ml-6">{{ plan.temp.customerType }}</div>
          <!-- <div class="mb-3">包含</div> -->
        </v-container>
        <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
          <v-icon size="30" icon="mdi-calendar-range" color="teal-accent-4" />
          <div class="text-h6 ml-1 mr-5">日期</div>
        </div>
        <v-container>
          <v-timeline side="end" align="start" class="justify-start">
            <v-timeline-item dot-color="green" size="small">
              <div class="text-subtitle-1">开始时间</div>
              <div class="text-subtitle-2">{{ plan.temp.startDate }}</div>
            </v-timeline-item>
            <v-timeline-item dot-color="pink" size="small">
              <div class="text-subtitle-1">结束时间</div>
              <div class="text-subtitle-2">{{ plan.temp.endDate }}</div>
            </v-timeline-item>
          </v-timeline>
        </v-container>
        <div class="d-flex flex-wrap justify-start align-center">
          <v-icon size="30" icon="mdi-currency-usd" color="teal-accent-4" />
          <div class="text-h6 ml-1 mr-5">预算</div>
        </div>
        <v-container>
          <div class="ml-6">{{ plan.temp.budget }}</div>
        </v-container>
        <div class="d-flex flex-wrap justify-start align-center">
          <v-icon
            size="30"
            icon="mdi-transit-connection-variant"
            color="teal-accent-4"
          />
          <div class="text-h6 ml-1 mr-5">交通工具</div>
        </div>
        <v-container>
          <div class="ml-6">
            {{ plan.temp.traffic }}
          </div>
        </v-container>
        <div class="d-flex flex-wrap justify-start align-center">
          <v-icon size="30" icon="mdi-star" color="teal-accent-4" />
          <div class="text-h6 ml-1 mr-5">兴趣爱好</div>
          <v-container>
            <v-chip-group column :disabled="true">
              <v-chip v-for="tag in plan.temp.tag" :key="tag">
                {{ tag.name }}
              </v-chip>
            </v-chip-group>
          </v-container>
        </div>
        <v-container> </v-container>
        <v-btn class="bg-black" @click="create" block color="teal-accent-4"
          >创建我的旅行计划</v-btn
        >
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";
import { generatePlan } from "@/utils/request/plan";
import axios from "axios";

const router = useRouter();
const plan = usePlanViewStore();

/**
 * @description 创建旅行计划
 */
async function create() {
  window.history.go(-(window.history.length - 1));
  router.push("/plan");
  plan.is.planGenerating = true;
  console.log(plan.is.planGenerating);
  await generatePlan(plan.temp);
  plan.is.planGenerating = false;
  reset();
}

/**
 * @description 重置数据
 */
function reset() {
  plan.temp = {
    destination: "",
    customerType: "",
    tag: [],
    startDate: "",
    endDate: "",
    budget: "",
    traffic: "",
  };
  plan.selected = {
    customerType: "",
    range: {
      start: "",
      end: "",
    },
    count: {
      children: 0,
      adult: 0,
    },
    budget: "",
    traffic: "",
    tag: [],
    activeCityInfo: {},
  };
}

onActivated(() => {
  plan.is.showAllDialog = true;
});
onDeactivated(() => {
  plan.is.showAllDialog = false;
});
</script>

<style scoped></style>
