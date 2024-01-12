import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";

export const usePlanViewStore = defineStore("planView", () => {
  let is = reactive({
    showAllDialog: false,
    showCityDialog: false,
    showCityInfoDialog: false,
    showPeopleDialog: false,
    showDateDialog: false,
    showMoneyDialog: false,
    showTrafficDialog: false,
    showHobbyDialog: false,
    showCreateModelDialog: false,
    showShowDialog: false,
    showChatDialog: false,
    showActionSheet: false,
    showCitySheet: false,
  });
  let config = reactive({});
  let temp = reactive({
    city: "",
    peopleModelActive: "",
    people: [],
    hobbies: [],
    toHobbies: [],
    beginDate: new Date(),
    endDate: new Date(),
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
