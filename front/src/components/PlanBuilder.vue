<template>
  <v-dialog
    v-model="plan.is.showBuilderDialog"
    fullscreen
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
  >
    <v-window v-model="plan.config.step" :disabled="true">
      <v-window-item :value="1">
        <CreateModel />
      </v-window-item>
      <v-window-item :value="2">
        <City v-if="plan.config.mode == 'normal'" />
      </v-window-item>
      <v-window-item :value="3">
        <People v-if="plan.config.mode == 'normal'" />
      </v-window-item>
      <v-window-item :value="4">
        <Date v-if="plan.config.mode == 'normal'" />
      </v-window-item>
      <v-window-item :value="5">
        <Money v-if="plan.config.mode == 'normal'" />
      </v-window-item>
      <v-window-item :value="6">
        <Traffic v-if="plan.config.mode == 'normal'" />
      </v-window-item>
      <v-window-item :value="7">
        <Hobby v-if="plan.config.mode == 'normal'" />
      </v-window-item>
      <v-window-item :value="8">
        <All v-if="plan.config.mode == 'normal'" />
      </v-window-item>
    </v-window>
  </v-dialog>
</template>

<script setup>
import { ref } from "vue";
import { usePlanViewStore } from "@/stores/planView";
import CreateModel from "@/components/PlanBuilder/CreateModel.vue";
import City from "@/components/PlanBuilder/Normal/City.vue";
import People from "@/components/PlanBuilder/Normal/People.vue";
import Date from "@/components/PlanBuilder/Normal/Date.vue";
import Money from "@/components/PlanBuilder/Normal/Money.vue";
import Hobby from "@/components/PlanBuilder/Normal/Hobby.vue";
import Traffic from "@/components/PlanBuilder/Normal/Traffic.vue";
import All from "@/components/PlanBuilder/Normal/All.vue";

const plan = usePlanViewStore();
</script>

<style scoped></style>
