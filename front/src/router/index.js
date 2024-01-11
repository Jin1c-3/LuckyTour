import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("@/views/HomeView.vue"),
    },
    {
      path: "/plan",
      name: "plan",
      component: () => import("@/views/PlanView.vue"),
    },
    {
      path: "/discover",
      name: "discover",
      component: () => import("@/views/DiscoverView.vue"),
    },
    {
      path: "/user",
      name: "user",
      component: () => import("@/views/UserView.vue"),
    },
  ],
});

export default router;
