<template>
  <v-dialog
    v-model="user.is.showUpdateDialog"
    :scrim="false"
    transition="dialog-bottom-transition"
    persistent
    fullscreen
    :close-on-back="false"
  >
    <v-card class="h-screen" :loading="loading">
      <div class="d-flex justify-start align-center mt-5">
        <v-btn
          variant="text"
          icon="mdi-arrow-left"
          class="ml-3"
          @click="router.back()"
        ></v-btn>
        <div class="text-h5 me-auto ml-4 title">个人信息</div>
        <v-btn class="mr-5" rounded @click="send">保存</v-btn>
      </div>
      <v-container>
        <div class="d-flex justify-center align-center mt-5 mb-5">
          <v-avatar size="150" class="mx-auto">
            <v-img :src="photoPath"></v-img>
            <v-btn
              icon="mdi-camera"
              class="avatarBtn"
              :size="60"
              @click="getGallery"
            ></v-btn>
          </v-avatar>
        </div>
        <v-text-field
          v-model="user.info.nickname"
          label="昵称"
          variant="solo"
        ></v-text-field>
        <v-text-field
          v-model="user.info.password"
          label="密码"
          variant="solo"
        ></v-text-field>
        <v-text-field
          v-model="user.info.phone"
          label="电话"
          variant="solo"
        ></v-text-field>
        <v-text-field
          v-model="user.info.email"
          label="邮箱"
          variant="solo"
        ></v-text-field>
        <v-text-field
          v-model="user.info.sex"
          label="性别"
          variant="solo"
        ></v-text-field>
        <v-text-field
          v-model="user.info.birthday"
          label="出生日期"
          variant="solo"
          type="date"
        ></v-text-field>
      </v-container>
    </v-card>
  </v-dialog>

  <v-snackbar
    v-model="snackbar"
    :timeout="2000"
    color="teal-accent-4"
    rounded="pill"
  >
    {{ content }}
  </v-snackbar>
</template>

<script setup>
import { ref, onActivated, onDeactivated } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useUserViewStore } from "@/stores/userView";
import { updateUserInfo, getUserInfo } from "@/utils/request/user";

const router = useRouter();
const user = useUserViewStore();

const items = [
  { text: "我的账户", icon: "mdi-clock" },
  { text: "帮助", icon: "mdi-account" },
  { text: "设置", icon: "mdi-cog" },
];

let snackbar = ref(false);
let content = ref("");
let loading = ref(false);
let photoPath = ref("");
let photo = ref();

/**
 *@description 发送请求
 */
async function send() {
  loading.value = true;
  let data = new FormData();
  data.append("avatarPic", photo.value);
  Object.assign(user.temp, user.info);
  console.log(user.temp);
  const result = await updateUserInfo(user.temp, data);
  reset();
  content.value = result.message;
  snackbar.value = true;
  loading.value = false;
  if (result.code == 200) {
    const userInfo = await getUserInfo();
    Object.assign(user.info, userInfo.data);
    router.back();
  }
}

/**
 *@description 重置
 */
function reset() {
  photoPath.value = "";
  user.temp.id = "";
  user.temp.nickname = "";
  user.temp.password = "";
  user.temp.phone = "";
  user.temp.email = "";
  user.temp.birthday = "";
  user.temp.sex = "";
}

/**
 *@description 从相册获取头像
 */
function getGallery() {
  plus.gallery.pick(
    function (path) {
      photoPath.value = path;
      // 读取文件
      plus.io.resolveLocalFileSystemURL(
        path,
        function (entry) {
          entry.file(
            function (file) {
              var fileReader = new plus.io.FileReader();
              fileReader.readAsDataURL(file);
              fileReader.onloadend = function (e) {
                var dataURL = e.target.result;
                var byteString = atob(dataURL.split(",")[1]);
                var mimeString = dataURL
                  .split(",")[0]
                  .split(":")[1]
                  .split(";")[0];
                var arrayBuffer = new ArrayBuffer(byteString.length);
                var uint8Array = new Uint8Array(arrayBuffer);
                for (var i = 0; i < byteString.length; i++) {
                  uint8Array[i] = byteString.charCodeAt(i);
                }
                var blob = new Blob([uint8Array], { type: mimeString });
                photo.value = new File([blob], file.name, { type: mimeString });
              };
            },
            function (e) {
              console.log("读取文件失败：" + e.message);
            }
          );
        },
        function (e) {
          console.log("读取文件失败：" + e.message);
        }
      );
    },
    function (e) {
      console.log("取消选择图片");
    },
    { filter: "image", premissionAlert: true }
  );
}

onActivated(() => {
  user.is.showUpdateDialog = true;
});
onDeactivated(() => {
  user.is.showUpdateDialog = false;
});
</script>

<style scoped>
.avatarBtn {
  position: absolute;
  opacity: 0.5;
}
</style>
