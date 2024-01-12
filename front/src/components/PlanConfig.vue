<template>
  <v-bottom-sheet v-model="plan.is.showActionSheet" persistent>
    <v-list>
      <v-list-subheader>操作</v-list-subheader>
      <v-list-item
        v-for="tile in tiles"
        :key="tile.title"
        :title="tile.title"
        @click="choose(tile.title)"
      >
        <template #prepend>
          <v-icon :icon="tile.icon" />
        </template>
      </v-list-item>
    </v-list>
  </v-bottom-sheet>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";

const router = useRouter();
const plan = usePlanViewStore();

const tiles = ref([
  { icon: "mdi-calendar-edit", title: "修改计划" },
  { icon: "mdi-delete", title: "删除计划" },
  { icon: "mdi-share-variant", title: "分享计划" },
]);

/**
 *
 * @param {string} title 选择的操作
 */
function choose(title) {
  router.back();
}

onActivated(() => {
  plan.is.showActionSheet = true;
});
onDeactivated(() => {
  plan.is.showActionSheet = false;
});
</script>

<style scoped></style>
