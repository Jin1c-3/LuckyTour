<template>
  <v-dialog
    v-model="plan.is.showChatDialog"
    fullscreen
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    :close-on-back="false"
  >
    <v-card class="h-screen">
      <v-container class="h-screen">
        <div class="d-flex flex-wrap align-center">
          <v-btn
            variant="text"
            icon="mdi-close"
            class="mr-5"
            @click="router.back()"
          ></v-btn>
          <div class="text-h5">云栖</div>
        </div>
        <div ref="content" class="contents mb-2">
          <div
            :class="`pa-4 ${content.set} msg mt-3`"
            v-for="content in contents"
            ref="text"
          >
            {{ content.value }}
          </div>
          <div class="loading ms-auto" v-if="loading">
            <div></div>
            <div></div>
            <div></div>
          </div>
        </div>
        <div class="d-flex align-center">
          <v-textarea
            v-model="ask"
            variant="outlined"
            class="mr-2"
            auto-grow
            rows="1"
          >
            <template v-slot:append>
              <v-btn
                id="menu-activator"
                icon="mdi-send"
                @click="send"
                class="mr-3"
              ></v-btn>
              <v-btn icon="mdi-menu" @click="upload"></v-btn>
              <v-file-input ref="fileInput" v-show="false"></v-file-input>
            </template>
          </v-textarea>
        </div>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated, onMounted, watch } from "vue";
import { RouterView, useRouter } from "vue-router";
import { usePlanViewStore } from "@/stores/planView";
import { chat } from "@/utils/request/plan";
import TypeIt from "typeit";

const router = useRouter();
const plan = usePlanViewStore();

const fileInput = ref(null);
const text = ref([]);
const content = ref(null);

const contents = ref([
  {
    set: " bg-teal-lighten-1 ms-auto rounded-xl rounded-be-0",
    value: "您好，我是你的智能旅行助手小云，请问你有什么旅行需求呢？",
  },
]);

let ask = ref("");
let isComplete = ref(true);
let loading = ref(false);

function upload() {
  fileInput.value.click();
}

/**
 * @description 发送请求
 */
async function send() {
  if (isComplete.value == false) return;
  isComplete.value = false;
  loading.value = true;
  contents.value.push({
    set: "bg-blue-grey-lighten-5 rounded-xl rounded-bs-0",
    value: ask.value,
  });
  let data = ask.value;
  ask.value = "";
  try {
    const result = await chat({ ask: data });
    if (result.code == 200) {
      contents.value.push({
        set: "bg-teal-lighten-1 ms-auto rounded-xl rounded-be-0",
        value: result.data,
      });
      setTimeout(() => {
        loading.value = false;
        new TypeIt(text.value[contents.value.length - 1], {
          cursor: false,
          afterStep: () => {
            content.value.scrollTop = content.value.scrollHeight;
          },
          afterComplete: () => {
            isComplete.value = true;
          },
        }).go();
      }, 0);
    }
  } catch (e) {
    isComplete.value = true;
    loading.value = false;
  }
  ask.value = "";
}

onMounted(() => {
  isComplete.value = false;
  setTimeout(() => {
    new TypeIt(text.value[contents.value.length - 1], {
      cursor: false,
      afterComplete: () => {
        isComplete.value = true;
      },
    }).go();
  }, 0);
});
watch(contents.value, () => {
  requestAnimationFrame(() => {
    content.value.scrollTop = content.value.scrollHeight;
  });
});
onActivated(() => {
  plan.is.showChatDialog = true;
});
onDeactivated(() => {
  plan.is.showChatDialog = false;
});
</script>

<style scoped>
.msg {
  width: fit-content;
  max-width: 70%;
}
.contents {
  height: 85%;
  overflow-y: auto;
}
.loading,
.loading > div {
  position: relative;
  box-sizing: border-box;
}

.loading {
  display: block;
  font-size: 0;
  color: #01772f;
}

.loading.la-dark {
  color: #06ff9b;
}

.loading > div {
  display: inline-block;
  float: none;
  background-color: currentColor;
  border: 0 solid currentColor;
}

.loading {
  width: 40px;
  height: 18px;
}

.loading > div {
  width: 5px;
  height: 5px;
  margin: 4px;
  border-radius: 100%;
  animation: ball-beat 0.7s -0.15s infinite linear;
}

.loading > div:nth-child(2n-1) {
  animation-delay: -0.5s;
}

@keyframes ball-beat {
  50% {
    opacity: 0.2;
    transform: scale(0.75);
  }

  100% {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
