<template>
  <v-dialog
    v-model="plan.is.showCityInfoDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen">
      <v-img
        :src="plan.selected.activeCityInfo.photos[0]"
        height="250"
        cover
      ></v-img>

      <v-container>
        <div class="text-h3 mb-3">
          {{ plan.selected.activeCityInfo.city }}
        </div>
        <v-divider class="border-opacity-25" />
        <div class="mt-2 mb-2 text-medium-emphasis">
          <v-icon icon="mdi-map-marker-radius-outline" />
          {{ plan.selected.activeCityInfo.province }},{{
            plan.selected.activeCityInfo.city
          }}
        </div>
        <div class="mb-3 text-medium-emphasis">
          <v-icon icon="mdi-xml" />
          {{ plan.selected.activeCityInfo.citycode }}
        </div>

        <v-slide-group :show-arrows="false">
          <v-slide-group-item
            v-for="photo in plan.selected.activeCityInfo.photos"
          >
            <v-sheet
              :height="150"
              :width="150"
              class="mx-2"
              rounded="xl"
              color="success"
            >
              <v-avatar size="150" rounded="xl">
                <v-img :src="photo" :aspect-ratio="1" cover />
              </v-avatar>
            </v-sheet>
          </v-slide-group-item>
        </v-slide-group>

        <div class="font-weight-light text-medium-emphasis my-3" tabindex="0">
          {{ plan.selected.activeCityInfo.description }}
        </div>
        <v-btn @click="next" color="teal-accent-4" block>下一步</v-btn>
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

/**
 * @description: 跳转到下一步
 */
function next() {
  router.push("/plan/people");
}

onActivated(() => {
  plan.is.showCityInfoDialog = true;
});
onDeactivated(() => {
  plan.is.showCityInfoDialog = false;
});
</script>

<style scoped></style>
