<template>
  <div class="d-flex justify-start align-center mt-5">
    <div class="text-h3 me-auto ml-4">首页</div>
    <v-avatar
      color="grey-darken-3"
      image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
      class="mr-5"
    ></v-avatar>
  </div>
  <v-container>
    <v-carousel hide-delimiters cycle height="250">
      <v-carousel-item
        v-for="(item, i) in items"
        :key="i"
        :src="item.src"
        cover
      ></v-carousel-item>
    </v-carousel>
  </v-container>
  <v-container fluid>
    <v-row dense>
      <v-col v-for="card in cards" :key="card.title" :cols="card.flex">
        <v-card>
          <v-img
            :src="card.src"
            class="align-end"
            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
            height="200px"
            cover
          >
            <v-card-title class="text-white" v-text="card.title"></v-card-title>
          </v-img>

          <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn
              size="small"
              color="surface-variant"
              variant="text"
              icon="mdi-heart"
            ></v-btn>

            <v-btn
              size="small"
              color="surface-variant"
              variant="text"
              icon="mdi-bookmark"
            ></v-btn>

            <v-btn
              size="small"
              color="surface-variant"
              variant="text"
              icon="mdi-share-variant"
            ></v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from "vue";

const items = ref([
  ({
    src: "https://cdn.vuetifyjs.com/images/carousel/squirrel.jpg",
  },
  {
    src: "https://cdn.vuetifyjs.com/images/carousel/sky.jpg",
  },
  {
    src: "https://cdn.vuetifyjs.com/images/carousel/bird.jpg",
  },
  {
    src: "https://cdn.vuetifyjs.com/images/carousel/planet.jpg",
  }),
]);
const cards = ref([
  {
    title: "Pre-fab homes",
    src: "https://cdn.vuetifyjs.com/images/cards/house.jpg",
    flex: 12,
  },
  {
    title: "Favorite road trips",
    src: "https://cdn.vuetifyjs.com/images/cards/road.jpg",
    flex: 6,
  },
  {
    title: "Best airlines",
    src: "https://cdn.vuetifyjs.com/images/cards/plane.jpg",
    flex: 6,
  },
]);
</script>

<style scoped></style>
