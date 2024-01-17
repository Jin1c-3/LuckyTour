import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

import "@mdi/font/css/materialdesignicons.css";
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import VCalendar from "v-calendar";
import "v-calendar/style.css";

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    iconfont: "mdi",
  },
});

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(vuetify);
app.use(VCalendar, {});

app.mount("#app");
