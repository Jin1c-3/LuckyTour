<template>
  <div class="d-flex justify-start align-center mt-5">
    <div class="text-h3 me-auto ml-4 title">计划</div>
    <v-btn
      variant="text"
      icon="mdi-message-text"
      class="mr-3"
      color="teal-accent-4"
      @click=""
    ></v-btn>
    <v-btn
      variant="text"
      icon="mdi-clipboard-edit-outline"
      class="mr-3"
      color="teal-accent-4"
      @click="router.push('/plan/city')"
    ></v-btn>
    <v-avatar
      color="grey-darken-3"
      image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
      class="mr-5"
    ></v-avatar>
  </div>
  <v-container>
    <v-card
      color="teal-accent-3"
      v-if="plan.is.planGenerating"
      :loading="plan.is.planGenerating"
      class="pa-2"
    >
      <v-card-title class="text-h5"
        >{{ plan.selected.activeCityInfo.city }}
      </v-card-title>
      <v-card-subtitle>请稍后，你的计划正在火速出炉中</v-card-subtitle>
    </v-card>
    <v-card
      class="mx-auto mt-4 mb-4"
      style="position: relative"
      @click="openCard(item.time)"
    >
      <v-card-title class="text-h5">我的旅行</v-card-title>
      <v-card-subtitle>城市</v-card-subtitle>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          variant="text"
          icon="mdi-dots-horizontal"
          @click="handleSpanClick"
        >
        </v-btn>
      </v-card-actions>
      <v-expand-transition>
        <div v-show="show">
          <v-divider></v-divider>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              variant="text"
              prepend-icon="mdi-calendar-edit"
              stacked
              @click=""
              >修改计划</v-btn
            >
            <v-btn variant="text" prepend-icon="mdi-delete" stacked @click=""
              >删除计划</v-btn
            >
            <v-btn
              variant="text"
              prepend-icon="mdi-share-variant"
              stacked
              @click=""
              >分享计划</v-btn
            >
          </v-card-actions>
        </div>
      </v-expand-transition>
    </v-card>
  </v-container>

  <v-snackbar
    v-model="snackbar"
    :timeout="2000"
    color="teal-accent-4"
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
let show = ref(false);

/**
 * @description: 展开附加信息
 */
function handleSpanClick(e) {
  e.stopPropagation();
  // router.push("/plan/config");
  show.value = !show.value;
}

/**
 * @description: 跳转到计划展示页面
 */
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
.title {
  color: var(--firstLevel-head-color);
}
</style>
