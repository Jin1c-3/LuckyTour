<template>
  <v-dialog
    v-model="plan.is.showCityDialog"
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
        <div class="text-h4 mt-5">目的地</div>
        <div class="text-subtitle-2 mb-5">选择你想要旅行的目的地？</div>
        <v-text-field
          v-model="search"
          clearable
          label="城市"
          variant="outlined"
          prepend-inner-icon="mdi-magnify"
          @update:model-value="searchInfo"
          base-color="teal-lighten-2"
          color="teal-darken-1"
        ></v-text-field>
        <div class="content">
          <v-card
            v-ripple
            class="my-2 pa-3"
            :elevation="0"
            @click="
              plan.temp.destination = item.city;
              plan.selected.activeCityInfo = item;
              router.push('/plan/cityInfo');
            "
            v-for="(item, index) in items"
          >
            <div class="d-flex flex-wrap justify-start align-center">
              <v-avatar class="mr-3" size="50" rounded="0">
                <v-img :src="item.photos[0]"></v-img>
              </v-avatar>
              <div>
                <div class="text-h6 font-weight-bold">{{ item.city }}</div>
                <div class="text-subtitle-2">
                  <v-icon
                    icon="mdi-map-marker-radius-outline"
                    :size="22"
                  ></v-icon
                  >{{ item.province }},{{ item.city }}
                </div>
              </div>
            </div>
          </v-card>
          <v-skeleton-loader
            v-if="loading"
            type="list-item-avatar-two-line"
            v-for="n in 9"
          >
          </v-skeleton-loader>
          <div
            class="d-flex flex-column justify-center align-center h-100"
            v-if="search == ''"
          >
            <v-icon
              icon="mdi-compass-outline"
              :size="40"
              color="teal-darken-1"
            ></v-icon>
            <div class="text-subtitle-1 ml-3 text-teal-darken-1">
              马上搜索发现更多目的地
            </div>
          </div>
        </div>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";
import { getCities } from "@/utils/request/plan";

const router = useRouter();
const plan = usePlanViewStore();

const items = ref([]);

let search = ref("");
let loading = ref(false);

async function searchInfo() {
  if (search.value == "") {
    loading.value = false;
    items.value = [];
    return;
  } else {
    loading.value = true;
    const result = await getCities({ character: search.value });
    items.value = result.data;
    loading.value = false;
  }
}

onActivated(() => {
  plan.is.showCityDialog = true;
});
onDeactivated(() => {
  plan.is.showCityDialog = false;
});
</script>

<style scoped>
.bom-btn {
  position: absolute;
  left: 50%;
  bottom: 5px;
  transform: translate(-50%, -50%);
}
.content {
  height: 560px;
  overflow-y: auto;
}
</style>
