import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";

export const usePlanViewStore = defineStore("planView", () => {
  let is = reactive({
    showBuilderDialog: false,
    showShowDialog: false,
    showChatDialog: false,
    showActionSheet: false,
    showCitySheet: false,
  });
  let config = reactive({
    step: 1,
    mode: "",
  });
  let temp = reactive({
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
    time: 0,
    interval: null,
  });
  let data = reactive({
    plans: [],
    activePlanInfo: {
      dates: [],
      all: {},
    },
    createPlanList: [],
  });
  return {
    is,
    temp,
    config,
    data,
  };
});
