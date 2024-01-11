<template>
  <div class="d-flex justify-start align-center mt-5 mb-4">
    <div class="text-h3 me-auto ml-4">用户</div>
  </div>
  <v-card
    class="mx-auto w-100"
    height="200"
    image="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
    theme="dark"
  >
    <div class="d-flex justify-start align-center" style="height: 200px">
      <v-avatar
        color="grey-darken-3"
        image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
        size="120"
        class="ml-5 mr-5"
      ></v-avatar>
      <div>
        <div class="text-h4 mb-3">张三</div>
        <div>上海市</div>
      </div>
    </div>
  </v-card>
</template>

<script setup>
import { ref } from "vue";
</script>

<style scoped></style>
