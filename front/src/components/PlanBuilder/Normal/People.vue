<template>
  <v-card class="h-screen">
    <div class="d-flex flex-wrap">
      <v-btn
        variant="text"
        icon="mdi-arrow-left"
        class="ml-5 mt-5 me-auto"
        @click="plan.config.step--"
      ></v-btn>
    </div>
    <div class="full-scroll">
      <div class="text-h4 me-auto ml-5 mt-5">旅客</div>
      <div class="text-subtitle-2 me-auto ml-5 mb-5">
        哪些人会加入这场旅途？
      </div>
      <div class="text-h6 ml-5">形式</div>
      <v-item-group selected-class="bg-black" class="w-75 mx-auto">
        <v-item v-slot="{ selectedClass, toggle }" v-for="item in items">
          <v-card
            :class="['d-flex align-center mt-5 mb-5', selectedClass]"
            dark
            height="100"
            @click="
              plan.temp.peopleModelActive = item.name;
              toggle();
            "
            variant="outlined"
          >
            <div>
              <div class="text-h5 ml-5">
                {{ item.name }}
              </div>
              <div class="text-subtitle-2 ml-5">
                {{ item.description }}
              </div>
            </div>

            <v-icon size="50" class="ml-auto mr-5">{{ item.icon }}</v-icon>
          </v-card>
        </v-item>
      </v-item-group>
      <div class="text-h6 ml-5">包含</div>
      <div class="w-75 mx-auto">
        <v-chip-group
          multiple
          selected-class="select-chip"
          column
          filter
          v-model="plan.temp.people"
        >
          <v-chip v-for="tag in tags" variant="outlined" :key="tag">
            {{ tag }}
          </v-chip>
        </v-chip-group>
      </div>
      <div class="mb-5"></div>
    </div>

    <v-btn
      class="bom-btn bg-black"
      @click="
        plan.config.step++;
        getTags();
      "
      width="300"
      >下一步</v-btn
    >
  </v-card>
</template>

<script setup>
import { ref } from "vue";
import { usePlanViewStore } from "@/stores/planView";

const plan = usePlanViewStore();

const items = ref([
  {
    name: "单人",
    description: "独享一个人的旅行",
    icon: "mdi-account",
  },
  {
    name: "情侣",
    description: "两个人的甜蜜旅程",
    icon: "mdi-account-multiple",
  },
  {
    name: "家庭",
    description: "三个人及以上的温馨旅行",
    icon: "mdi-account-group",
  },
  {
    name: "朋友",
    description: "三个人及以上的有趣旅行",
    icon: "mdi-account-group-outline",
  },
]);
const tags = ["小孩", "老人", "成人", "健身达人", "行动不便的人"];

function getTags() {
  let data = [];
  plan.temp.people.forEach((tag) => {
    data.push(tags[tag]);
  });
  plan.temp.people = data;
}
</script>

<style scoped>
.bom-btn {
  position: absolute;
  left: 50%;
  bottom: 5px;
  transform: translate(-50%, -50%);
}
.full-scroll {
  overflow-y: scroll;
  height: 85%;
}
</style>
