<template>
  <v-card class="h-screen">
    <div class="d-flex flex-wrap">
      <v-btn
        variant="text"
        icon="mdi-close"
        class="ml-5 mt-5 mr-5"
        @click="plan.is.showBuilderDialog = false"
      ></v-btn>
    </div>

    <div class="text-h4 me-auto ml-5 mt-5">模式</div>
    <div class="text-subtitle-2 me-auto ml-5 mb-5">
      从以下几种模式中选择一种来创建属于你的旅行计划
    </div>
    <v-container>
      <v-card
        v-ripple
        variant="outlined"
        class="mx-auto mb-5"
        title="普通模式"
        text="你需要做出一系列选择，我们会根据你的选择信息为你生成旅行计划"
        @click="
          plan.config.mode = 'normal';
          plan.config.step++;
        "
      >
      </v-card>
      <v-card
        v-ripple
        variant="outlined"
        class="mx-auto mb-5"
        title="自由模式"
        text="你可以自由的与AI交流，我们会根据交流的内容为你生成旅行计划"
        @click="
          plan.config.mode = 'free';
          plan.config.step++;
        "
      >
      </v-card>
      <v-card
        v-ripple
        variant="outlined"
        class="mx-auto mb-5"
        title="创造模式"
        text="你可以添加图片，视频，文本，语音，我们会根据你的上传和描述为你生成旅行计划"
        @click="
          plan.config.mode = 'create';
          plan.config.step++;
        "
      >
      </v-card>
    </v-container>
  </v-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { usePlanViewStore } from "@/stores/planView";
// import { useBackStore } from "@/stores/back";

const plan = usePlanViewStore();
// const back = useBackStore();

// onMounted(() => {
//   back.pushHistory();
//   back.addPopstateListener(() => {
//     console.log("listened");
//     // plan.is.showBuilderDialog = false;
//   });
// });

// onUnmounted(() => {
//   back.removePopstateListener(() => {
//     console.log("remove");
//   });
// });
</script>

<style scoped></style>
