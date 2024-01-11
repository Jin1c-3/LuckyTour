<template>
  <div class="d-flex justify-start align-center mt-5">
    <div class="text-h3 me-auto ml-4">计划</div>
    <v-btn
      variant="text"
      icon="mdi-plus"
      class="mr-5"
      @click="plan.is.showBuilderDialog = true"
    ></v-btn>
    <v-avatar
      color="grey-darken-3"
      image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
      class="mr-5"
    ></v-avatar>
  </div>
  <v-container>
    <v-card
      class="mx-auto mt-4 mb-4"
      style="position: relative"
      image="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
      color="#3C87CD"
      @click="openCard(item.time)"
      v-for="item in plan.data.createPlanList"
    >
      <v-card-title class="text-h5">我的旅行</v-card-title>
      <v-card-subtitle>{{ item.city.city }}</v-card-subtitle>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          variant="text"
          icon="mdi-dots-horizontal"
          @click="handleSpanClick"
        >
        </v-btn>
      </v-card-actions>
      <v-progress-circular
        v-if="item.time <= 100"
        :rotate="360"
        :size="60"
        :width="10"
        :model-value="item.time"
        color="white"
        class="progress-circular"
      >
        {{ item.time }}
      </v-progress-circular>
    </v-card>
  </v-container>

  <v-bottom-sheet v-model="plan.is.showActionSheet">
    <v-list>
      <v-list-subheader>操作</v-list-subheader>

      <v-list-item
        v-for="tile in tiles"
        :key="tile.title"
        :title="tile.title"
        @click="plan.is.showActionSheet = false"
      >
        <template #prepend>
          <v-icon :icon="tile.icon" />
        </template>
      </v-list-item>
    </v-list>
  </v-bottom-sheet>

  <v-snackbar v-model="snackbar" :timeout="2000" class="snackbar">
    计划生成中，请稍后
  </v-snackbar>

  <PlanBuilder />

  <PlanShow />
</template>

<script setup>
import { ref } from "vue";
import PlanBuilder from "@/components/PlanBuilder.vue";
import PlanShow from "@/components/PlanShow.vue";
import { usePlanViewStore } from "@/stores/planView";

const plan = usePlanViewStore();
const tiles = ref([
  { icon: "mdi-calendar-edit", title: "修改计划" },
  { icon: "mdi-delete", title: "删除计划" },
  { icon: "mdi-share-variant", title: "分享计划" },
]);
let snackbar = ref(false);

const handleSpanClick = (e) => {
  e.stopPropagation();
  plan.is.showActionSheet = true;
};

function openCard(time) {
  if (time >= 100) {
    plan.is.showShowDialog = true;
  } else {
    snackbar.value = true;
  }
}
</script>

<style scoped>
.progress-circular {
  position: absolute;
  top: 10px;
  right: 10px;
}
.snackbar {
  transform: translateY(-850px);
}
</style>
