<template>
  <v-dialog
    v-model="plan.is.showChatDialog"
    fullscreen
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    :close-on-back="false"
  >
    <v-card class="h-screen"
      ><div class="d-flex flex-wrap align-center mt-5">
        <v-btn
          variant="text"
          icon="mdi-close"
          class="ml-5 mr-5"
          @click="router.back()"
        ></v-btn>
        <div class="text-h5">云栖</div>
      </div>
      <div class="pa-4 bg-secondary rounded-xl rounded-be-0 w-50 ms-auto mr-3">
        你好！我是小云，你的AI智能助手，请问有什么可以帮助你的？
      </div>
    </v-card>
    <div class="d-flex flex-wrap align-center bom" style="width: 90%">
      <v-text-field
        variant="outlined"
        prepend-inner-icon="mdi-map-marker"
        clearable
        rounded
        style="transform: translateY(10px)"
        class="mr-3"
      ></v-text-field>
      <v-btn id="menu-activator" icon="mdi-send" @click="" class="mr-4"></v-btn>
      <v-btn icon="mdi-folder-arrow-up-outline" @click="update"></v-btn>
      <v-file-input
        label="File input"
        ref="fileInput"
        v-show="false"
      ></v-file-input>
    </div>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";

const router = useRouter();
const plan = usePlanViewStore();
const items = [
  { title: "Click Me" },
  { title: "Click Me" },
  { title: "Click Me" },
  { title: "Click Me 2" },
];
const fileInput = ref(null);

function update() {
  fileInput.value.click();
}

onActivated(() => {
  plan.is.showChatDialog = true;
});
onDeactivated(() => {
  plan.is.showChatDialog = false;
});
</script>

<style scoped>
.bom {
  position: absolute;
  left: 50%;
  bottom: -20px;
  transform: translate(-50%, -50%);
  width: 50%;
}
.full-scroll {
  overflow-y: scroll;
  height: 75%;
}
</style>
