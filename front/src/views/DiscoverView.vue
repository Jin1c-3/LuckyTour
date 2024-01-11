<template>
  <div class="d-flex justify-start align-center mt-5">
    <div class="text-h3 me-auto ml-4">发现</div>
    <v-avatar
      color="grey-darken-3"
      image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
      class="mr-5"
    ></v-avatar>
  </div>
  <v-container fluid>
    <v-card
      border
      class="mb-2"
      density="compact"
      prepend-avatar="https://randomuser.me/api/portraits/women/10.jpg"
      subtitle="Salsa, merengue, y cumbia"
      title="Cuba"
      variant="text"
    >
      <v-img
        src="https://picsum.photos/512/128?image=660"
        height="128"
        cover
      ></v-img>

      <v-card-text>
        During my last trip to South America, I spent 2 weeks traveling through
        Patagonia in Chile.
      </v-card-text>

      <template v-slot:actions>
        <v-btn color="primary" variant="text">View More</v-btn>

        <v-btn color="primary" variant="text">See in Map</v-btn>
      </template>
    </v-card>

    <v-card
      border
      class="mb-2"
      density="comfortable"
      prepend-avatar="https://randomuser.me/api/portraits/women/17.jpg"
      subtitle="Salsa, merengue, y cumbia"
      title="Florida"
      variant="text"
    >
      <v-img
        src="https://picsum.photos/512/128?random"
        height="128"
        cover
      ></v-img>

      <v-card-text>
        During my last trip to Florida, I spent 2 weeks traveling through the
        Everglades.
      </v-card-text>

      <template v-slot:actions>
        <v-btn color="primary" variant="text">View More</v-btn>

        <v-btn color="primary" variant="text">See in Map</v-btn>
      </template>
    </v-card>
    <v-card
      border
      class="mb-2"
      density="compact"
      prepend-avatar="https://randomuser.me/api/portraits/women/10.jpg"
      subtitle="Salsa, merengue, y cumbia"
      title="Cuba"
      variant="text"
    >
      <v-img
        src="https://picsum.photos/512/128?image=660"
        height="128"
        cover
      ></v-img>

      <v-card-text>
        During my last trip to South America, I spent 2 weeks traveling through
        Patagonia in Chile.
      </v-card-text>

      <template v-slot:actions>
        <v-btn color="primary" variant="text">View More</v-btn>

        <v-btn color="primary" variant="text">See in Map</v-btn>
      </template>
    </v-card>

    <v-card
      border
      class="mb-2"
      density="comfortable"
      prepend-avatar="https://randomuser.me/api/portraits/women/17.jpg"
      subtitle="Salsa, merengue, y cumbia"
      title="Florida"
      variant="text"
    >
      <v-img
        src="https://picsum.photos/512/128?random"
        height="128"
        cover
      ></v-img>

      <v-card-text>
        During my last trip to Florida, I spent 2 weeks traveling through the
        Everglades.
      </v-card-text>

      <template v-slot:actions>
        <v-btn color="primary" variant="text">View More</v-btn>

        <v-btn color="primary" variant="text">See in Map</v-btn>
      </template>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from "vue";
</script>

<style scoped></style>
