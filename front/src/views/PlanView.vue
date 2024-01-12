<template>
  <div class="d-flex justify-start align-center mt-5">
    <div class="text-h3 me-auto ml-4 title">计划</div>
    <v-btn
      variant="text"
      icon="mdi-plus"
      class="mr-5"
      @click="
        router.push('/plan/createModel');
        console.log(router.currentRoute.value.name);
      "
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

  <v-snackbar
    v-model="snackbar"
    :timeout="2000"
    class="snackbar"
    color="blue-grey"
    rounded="pill"
  >
    计划生成中，请稍后
  </v-snackbar>

  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :is="Component" />
    </keep-alive>
  </router-view>
</template>

<script setup>
import { ref, KeepAlive } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";

const router = useRouter();
const plan = usePlanViewStore();

let snackbar = ref(false);

function handleSpanClick(e) {
  e.stopPropagation();
  router.push("/plan/config");
}

function openCard(time) {
  if (time >= 100) {
    router.push("/plan/show");
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
  transform: translateY(-70px);
}
.title {
  color: var(--firstLevel-head-color);
}
</style>
