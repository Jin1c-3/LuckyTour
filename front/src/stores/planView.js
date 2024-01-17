import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";

export const usePlanViewStore = defineStore("planView", () => {
  let is = reactive({
    planGenerating: false,
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
    destination: "",
    customerType: "",
    tag: [],
    startDate: "",
    endDate: "",
    budget: "",
    traffic: "",
  });
  let selected = reactive({
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
  });
  let data = reactive({
    plans: [],
    activePlanInfo: {
      dates: [],
      all: {},
    },
  });
  return {
    is,
    temp,
    config,
    data,
    selected,
  };
});
