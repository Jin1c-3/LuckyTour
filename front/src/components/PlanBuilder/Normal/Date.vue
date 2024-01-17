<template>
  <v-dialog
    v-model="plan.is.showDateDialog"
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
        <div class="text-h4 mt-5">日期</div>
        <div class="text-subtitle-2 mb-5">什么时候来一场旅行？</div>
        <VDatePicker
          transparent
          borderless
          color="green"
          expanded
          title-position="left"
          :rows="3"
          :step="1"
          :min-date="new Date()"
          v-model.range.string="plan.selected.range"
          :masks="masks"
        />
        <v-btn @click="next" block color="teal-accent-4">下一步</v-btn>
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

const masks = ref({
  modelValue: "YYYY-MM-DD",
});

/**
 * @description: 设置日期
 */
function date() {
  plan.temp.startDate = plan.selected.range.start;
  plan.temp.endDate = plan.selected.range.end;
}

/**
 * @description: 最大日期
 */
function maxDate() {
  if (plan.selected.range.start) {
  }
}

/**
 * @description: 跳转到下一步
 */
function next() {
  if (plan.selected.range.start == "" || plan.selected.range.end == "") return;
  date();
  router.push("/plan/money");
}

onActivated(() => {
  plan.is.showDateDialog = true;
});
onDeactivated(() => {
  plan.is.showDateDialog = false;
});
</script>

<style scoped></style>
