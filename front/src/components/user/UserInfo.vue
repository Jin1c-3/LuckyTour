<template>
  <v-dialog
    v-model="user.is.showUserInfoDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen">
      <v-container>
        <div
          class="d-flex justify-start align-center"
          style="height: 200px"
          v-if="user.status.login"
        >
          <v-avatar
            color="grey-darken-3"
            image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
            size="120"
            class="ml-5 mr-5"
          ></v-avatar>
          <div>
            <div class="text-h4 mb-3">Admin</div>
            <div>山东省，威海市，环翠区</div>
          </div>
        </div>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useUserViewStore } from "@/stores/userView";

const router = useRouter();
const user = useUserViewStore();

onActivated(() => {
  user.is.showUserInfoDialog = true;
});
onDeactivated(() => {
  user.is.showUserInfoDialog = false;
});
</script>

<style scoped></style>
