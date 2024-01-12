<template>
  <v-dialog
    v-model="plan.is.showShowDialog"
    fullscreen
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    :close-on-back="false"
  >
    <v-card class="h-screen"
      ><div class="d-flex flex-wrap">
        <v-btn
          variant="text"
          icon="mdi-close"
          class="ml-5 mt-5 mr-5"
          @click="router.back()"
        ></v-btn>
      </div>
      <div class="d-flex justify-start align-center">
        <div>
          <div class="text-h4 me-auto ml-5 mt-5">我的旅行</div>
          <div class="text-subtitle-2 me-auto ml-5 mb-5">山东威海</div>
        </div>
        <v-spacer></v-spacer>
        <div>
          <v-rating
            half-increments
            hover
            :length="5"
            :size="32"
            :model-value="3"
            color="primary"
            active-color="warning"
          />
          <div class="text-subtitle-2 ml-4">快给本次计划打个分</div>
        </div>
      </div>

      <v-tabs v-model="tab" align-tabs="center" center-active show-arrows>
        <v-tab :value="date" v-for="date in plan.data.activePlanInfo.dates">{{
          date
        }}</v-tab>
      </v-tabs>
      <div class="full-scroll">
        <v-window v-model="tab">
          <v-window-item
            v-for="date in plan.data.activePlanInfo.dates"
            :value="date"
          >
            <v-timeline
              align="start"
              side="end"
              style="transform: translateX(-18px)"
            >
              <v-timeline-item
                v-for="(item, i) in plan.data.activePlanInfo.all[tab]"
                :key="i"
                :dot-color="item.color"
                :icon="item.icon"
                fill-dot
              >
                <v-card width="350">
                  <v-card-subtitle class="pt-4"> Number 10 </v-card-subtitle>

                  <v-card-text>
                    <div>Whitehaven Beach</div>

                    <div>Whitsunday Island, Whitsunday Islands</div>
                  </v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn :color="item.color" variant="outlined">
                      Button
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-timeline-item>
            </v-timeline>
          </v-window-item>
        </v-window>
      </div>

      <v-btn
        class="bom-btn"
        icon="mdi-forum"
        @click="router.push('/plan/chat')"
      ></v-btn>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";
import PlanChat from "@/components/PlanChat.vue";

const router = useRouter();
const plan = usePlanViewStore();
let tab = ref("");

const new_result = {
  "2023-12-01": [
    {
      parent: "B0FFF6BKO3",
      address: "海滨北路101-2号刘公岛风景名胜区内(西侧)",
      business: {
        opentime_today: "07:30-15:30",
        keytag: "博物馆",
        rating: "4.7",
        alias: "甲午风云录陈列馆",
        tel: "0631-5226357;0631-5324184",
        rectag: "博物馆",
        opentime_week: "07:30-15:30",
      },
      distance: "",
      pcode: "370000",
      adcode: "371002",
      pname: "山东省",
      cityname: "威海市",
      type: "科教文化服务;展览馆;展览馆",
      photos: [
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/75fabd00b9a504f4d455117422c67911",
        },
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/ff128a32205a2a17ff74991afff2817d",
        },
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/87399562e8f54128d14ea6c272f8d2a1",
        },
      ],
      typecode: "140200",
      adname: "环翠区",
      citycode: "0631",
      name: "中国甲午战争博物院",
      location: "122.181272,37.501421",
      id: "B0FFFV52KG",
    },
    {
      parent: "",
      address: "统一北路与益海路交叉口西80米",
      business: {
        opentime_today: "08:00-20:00",
        keytag: "美术馆",
        rating: "4.7",
        rectag: "美术馆",
        opentime_week: "08:00-20:00",
      },
      distance: "",
      pcode: "370000",
      adcode: "371002",
      pname: "山东省",
      cityname: "威海市",
      type: "科教文化服务;科教文化场所;科教文化场所",
      photos: [
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/7ae16f806c8ac1e0b687b8652294f6f7",
        },
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/69714ec6660a153712e69692de24d923",
        },
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/e502ac9cf6d0b486ee0dd16035f1a2da",
        },
      ],
      typecode: "140000",
      adname: "环翠区",
      citycode: "0631",
      name: "威海天和美术馆",
      location: "122.113268,37.538234",
      id: "B0GK4XR05S",
    },
  ],
  "2023-12-02": [
    {
      parent: "",
      address: "文化西路180号",
      business: {
        keytag: "大学",
        alias: "威海山东大学|威海山大|山东大学威海",
        tel: "0631-5688282",
        rectag: "大学",
      },
      distance: "",
      pcode: "370000",
      adcode: "371002",
      pname: "山东省",
      cityname: "威海市",
      type: "科教文化服务;学校;高等院校",
      photos: [
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/2d5260e4165fcba83865b25c9b90342a",
        },
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/1671e9cee17fcf138274a524df75e8df",
        },
        {
          title: "",
          url: "http://store.is.autonavi.com/showpic/d8b438fdd4cbf7c50e4617e079deb128",
        },
      ],
      typecode: "141201",
      adname: "环翠区",
      citycode: "0631",
      children: [
        {
          typecode: "150904",
          address: "文山路与山海北路交叉口东南200米",
          subtype: "停车场",
          sname: " 停车场 ",
          name: "山东大学威海校区停车场",
          location: "122.065352,37.533302",
          id: "B0FFHY5HDN",
        },
        {
          typecode: "150904",
          address: "南环东路山东大学(威海校区)",
          subtype: "停车场",
          sname: " 停车场 ",
          name: "山东大学威海校区停车场",
          location: "122.061866,37.528284",
          id: "B0FFHY5CVF",
        },
        {
          typecode: "150905",
          address: "文化西路180号山东大学威海校区内",
          subtype: "停车场",
          sname: " 停车场 ",
          name: "山东大学威海校区停车场",
          location: "122.064878,37.530146",
          id: "B0FFG8WHNT",
        },
      ],
      name: "山东大学威海校区",
      location: "122.058225,37.532313",
      id: "B0FFF5M7VX",
    },
  ],
};
plan.data.activePlanInfo.all = new_result;
let all = ref([]);
for (let key in plan.data.activePlanInfo.all) {
  plan.data.activePlanInfo.dates.push(key);
  all.value.push(plan.data.activePlanInfo.all[key]);
}
plan.data.activePlanInfo.all = all.value;

onActivated(() => {
  plan.is.showShowDialog = true;
});
onDeactivated(() => {
  plan.is.showShowDialog = false;
});
</script>

<style scoped>
.bom-btn {
  position: absolute;
  right: 15px;
  bottom: 15px;
}
.full-scroll {
  overflow-y: scroll;
  height: 75%;
}
</style>
