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
      <div class="text-h4 me-auto ml-5 mt-5">预览</div>
      <div class="text-subtitle-2 me-auto ml-5 mb-5">确认创建信息是否正确</div>
      <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
        <v-icon size="30" class="ml-5 mr-1">mdi-map-marker</v-icon>
        <div class="text-h6 ml-1 mr-5">城市</div>
      </div>
      <div class="d-flex flex-wrap justify-start align-center ml-12">
        <v-avatar class="ma-3" size="50" rounded="0">
          <v-img
            src="https://cdn.vuetifyjs.com/images/cards/halcyon.png"
          ></v-img>
        </v-avatar>
        <div>
          <div class="text-h4 mb-3">威海</div>
          <div>山东省 威海市</div>
        </div>
      </div>
      <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
        <v-icon size="30" class="ml-5 mr-1">mdi-account</v-icon>
        <div class="text-h6 ml-1 mr-5">旅客</div>
      </div>
      <div class="ml-12">
        <div class="ml-3 mb-3">类型</div>
        <div class="ml-9 mb-3">{{ plan.temp.peopleModelActive }}</div>
        <div class="ml-3 mb-3">包含</div>
        <div class="d-flex ml-9">
          <div v-for="item in plan.temp.people" class="mr-2 mb-3">
            {{ item }}
          </div>
        </div>
      </div>
      <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
        <v-icon size="30" class="ml-5 mr-1">mdi-calendar-range</v-icon>
        <div class="text-h6 ml-1 mr-5">日期</div>
      </div>
      <div class="ml-12">
        <div class="ml-3 mb-3">开始</div>
        <div class="ml-9 mb-3">{{ plan.temp.beginDate }}</div>
        <div class="ml-3 mb-3">结束</div>
        <div class="ml-9 mb-3">
          {{ plan.temp.endDate }}
        </div>
      </div>
      <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
        <v-icon size="30" class="ml-5 mr-1">mdi-currency-usd</v-icon>
        <div class="text-h6 ml-1 mr-5">预算</div>
      </div>
      <div class="ml-12">
        <div class="ml-3 mb-3">类型</div>
        <div class="ml-9 mb-3">{{ plan.temp.costModelActive }}</div>
        <div class="ml-3 mb-3">价位</div>
        <div class="ml-9 mb-3">{{ plan.temp.cost }}</div>
      </div>
      <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
        <v-icon size="30" class="ml-5 mr-1"
          >mdi-transit-connection-variant</v-icon
        >
        <div class="text-h6 ml-1 mr-5">交通工具</div>
      </div>
      <div class="ml-12">
        <div class="d-flex ml-3">
          {{ plan.temp.trafficModelActive }}
        </div>
      </div>
      <div class="d-flex flex-wrap justify-start align-center mb-3 mt-3">
        <v-icon size="30" class="ml-5 mr-1">mdi-star</v-icon>
        <div class="text-h6 ml-1 mr-5">兴趣爱好</div>
      </div>
      <div class="ml-12">
        <div class="d-flex ml-3">
          <div v-for="item in plan.temp.hobbies" class="mr-2 mb-3">
            {{ item }}
          </div>
        </div>
      </div>
    </div>

    <v-btn class="bom-btn bg-black" @click="create" width="300"
      >创建我的旅行计划</v-btn
    >
  </v-card>
</template>

<script setup>
import { ref } from "vue";
import { usePlanViewStore } from "@/stores/planView";
import axios from "axios";

const plan = usePlanViewStore();

function create() {
  //发送请求
  // axios
  //   .post("http://localhost:8080/api/plan", {
  //     city: plan.temp.city,
  //     peopleModelActive: plan.temp.peopleModelActive,
  //     people: plan.temp.people,
  //     hobbies: plan.temp.hobbies,
  //     toHobbies: plan.temp.toHobbies,
  //     beginDate: plan.temp.beginDate,
  //     endDate: plan.temp.endDate,
  //     costModelActive: plan.temp.costModelActive,
  //     cost: plan.temp.cost,
  //     trafficModelActive: plan.temp.trafficModelActive,
  //   })
  //   .then((res) => {
  //     console.log(res);
  //   })
  //   .catch((err) => {
  //     console.log(err);
  //   });
  plan.data.createPlanList.push(plan.temp);
  plan.data.createPlanList[plan.data.createPlanList.length - 1].interval =
    setInterval(() => {
      if (
        plan.data.createPlanList[plan.data.createPlanList.length - 1].time > 100
      ) {
        clearInterval(
          plan.data.createPlanList[plan.data.createPlanList.length - 1].interval
        );
      }
      plan.data.createPlanList[plan.data.createPlanList.length - 1].time++;
    }, 500);
  reset();
}
function reset() {
  plan.is.showBuilderDialog = false;
  plan.config.step = 1;
  plan.temp = {
    city: "",
    peopleModelActive: "",
    people: [],
    hobbies: [],
    toHobbies: [],
    beginDate: null,
    endDate: null,
    costModelActive: "经济",
    cost: 0,
    trafficModelActive: "",
  };
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
