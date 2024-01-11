<template>
  <div class="d-flex justify-start align-center mt-5">
    <div class="text-h3 me-auto ml-4">设置</div>
    <v-btn variant="text" icon="mdi-plus" class="mr-5" @click=""></v-btn>
  </div>
</template>

<script setup>
import { ref } from "vue";
// import { useRouter } from "vue-router";

// const router = useRouter();
// function pushMessage() {
//   plus.push.addEventListener(
//     "click",
//     (msg) => {
//       console.log("click", msg);
//       router.push("/plan");
//     },
//     false
//   );
//   let content = "您有一条新的消息~";
//   let options = {
//     cover: false,
//     when: new Date(),
//     title: "通知消息",
//   };
//   let body = {
//     id: "id",
//     key: "key",
//   };
//   let payload = JSON.stringify(body);
//   plus.push.createMessage(content, options, payload);
//   plus.runtime.setBadgeNumber(1);
// }
</script>

<style scoped></style>
