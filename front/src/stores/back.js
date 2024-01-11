import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useBackStore = defineStore("back", () => {
  function pushHistory() {
    window.history.pushState(null, null, "");
  }

  function addPopstateListener(back) {
    if (window.addEventListener) {
      window.addEventListener("popstate", back, true);
    } else {
      window.attachEvent("popstate", back);
    }
  }

  function removePopstateListener(back) {
    if (window.removeEventListener) {
      window.removeEventListener("popstate", back, true);
    } else {
      window.detachEvent("popstate", back);
    }
  }

  return { pushHistory, addPopstateListener, removePopstateListener };
});
