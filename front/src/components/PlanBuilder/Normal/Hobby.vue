<template>
  <v-dialog
    v-model="plan.is.showHobbyDialog"
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
        <div class="text-h4 mt-5">兴趣</div>
        <div class="text-subtitle-2 mb-5">选择你感兴趣的标签</div>
        <v-chip-group
          multiple
          selected-class="select-chip"
          column
          filter
          v-model="plan.selected.tag"
          class="mb-5"
          variant="outlined"
        >
          <v-chip v-for="tag in tags" :key="tag" :append-avatar="tag.url">
            {{ tag.name }}
          </v-chip>
        </v-chip-group>
        <v-btn
          @click="
            router.push('/plan/all');
            getTags();
          "
          block
          color="teal-accent-4"
          >下一步</v-btn
        >
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
const tags = [
  {
    name: "美食",
    url: new URL("@/assets/images/tag/food.svg", import.meta.url).href,
  },
  {
    name: "购物",
    url: new URL("@/assets/images/tag/shopping.svg", import.meta.url).href,
  },
  {
    name: "自然",
    url: new URL("@/assets/images/tag/nature.svg", import.meta.url).href,
  },
  {
    name: "历史",
    url: new URL("@/assets/images/tag/culture.svg", import.meta.url).href,
  },
  {
    name: "艺术",
    url: new URL("@/assets/images/tag/art.svg", import.meta.url).href,
  },
  {
    name: "音乐",
    url: new URL("@/assets/images/tag/music.svg", import.meta.url).href,
  },
  {
    name: "运动",
    url: new URL("@/assets/images/tag/sport.svg", import.meta.url).href,
  },
  {
    name: "户外",
    url: new URL("@/assets/images/tag/outdoor.svg", import.meta.url).href,
  },
  {
    name: "夜生活",
    url: new URL("@/assets/images/tag/beer.svg", import.meta.url).href,
  },
];

function getTags() {
  let data = [];
  plan.selected.tag.forEach((tag) => {
    data.push(tags[tag]);
  });
  plan.temp.tag = data;
}

onActivated(() => {
  plan.is.showHobbyDialog = true;
});
onDeactivated(() => {
  plan.is.showHobbyDialog = false;
});
</script>

<style scoped>
.select-chip {
  color: #26a69a;
  background-color: rgb(255, 255, 255);
}
</style>
