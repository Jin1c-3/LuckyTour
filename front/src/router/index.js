import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/home",
    },
    {
      path: "/home",
      name: "home",
      component: () => import("@/views/HomeView.vue"),
    },
    {
      path: "/plan",
      name: "plan",
      component: () => import("@/views/PlanView.vue"),
      children: [
        {
          path: "createModel",
          name: "createModel",
          component: () => import("@/components/PlanBuilder/CreateModel.vue"),
        },
        {
          path: "city",
          name: "city",
          component: () => import("@/components/PlanBuilder/Normal/City.vue"),
        },
        {
          path: "cityInfo",
          name: "cityInfo",
          component: () =>
            import("@/components/PlanBuilder/Normal/CityInfo.vue"),
        },
        {
          path: "people",
          name: "people",
          component: () => import("@/components/PlanBuilder/Normal/People.vue"),
        },
        {
          path: "date",
          name: "date",
          component: () => import("@/components/PlanBuilder/Normal/Date.vue"),
        },
        {
          path: "money",
          name: "money",
          component: () => import("@/components/PlanBuilder/Normal/Money.vue"),
        },
        {
          path: "traffic",
          name: "traffic",
          component: () =>
            import("@/components/PlanBuilder/Normal/Traffic.vue"),
        },
        {
          path: "hobby",
          name: "hobby",
          component: () => import("@/components/PlanBuilder/Normal/Hobby.vue"),
        },
        {
          path: "all",
          name: "all",
          component: () => import("@/components/PlanBuilder/Normal/All.vue"),
        },
        {
          path: "show",
          name: "planShow",
          component: () => import("@/components/PlanShow.vue"),
        },
        {
          path: "chat",
          name: "palnChat",
          component: () => import("@/components/PlanChat.vue"),
        },
        {
          path: "config",
          name: "planConfig",
          component: () => import("@/components/PlanConfig.vue"),
        },
      ],
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
      children: [
        {
          path: "login",
          name: "login",
          component: () => import("@/components/Login.vue"),
        },
        {
          path: "register",
          name: "register",
          component: () => import("@/components/Register.vue"),
        },
        {
          path: "validateCode",
          name: "validateCode",
          component: () => import("@/components/ValidationCode.vue"),
        },
      ],
    },
    {
      path: "/404",
      name: "404",
      component: () => import("@/components/more/NotFound.vue"),
    },
    {
      path: "/:catchAll(.*)",
      redirect: "/404",
    },
  ],
});

export default router;
