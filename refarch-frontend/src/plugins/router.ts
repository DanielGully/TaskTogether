// Composables
import { createRouter, createWebHashHistory } from "vue-router";

// Importiere die Konstanten
import {
  ROUTES_GETSTARTED,
  ROUTES_HOME,
  ROUTES_MIR_ZUGEWIESENE_TODOS,
  ROUTES_NOTIZENANSICHT,
  ROUTES_PERSOENLICHE_TODOS,
} from "@/constants";
import GetStartedView from "@/views/GetStartedView.vue";
import HomeView from "@/views/HomeView.vue";
import MirZugewieseneTodos from "@/views/MirZugewiesenAnsicht.vue";
import Notizenansicht from "@/views/NotizenAnsicht.vue";
import PersoenlicheTodos from "@/views/PersoenlicheAnsicht.vue";

// Definiere die Routen
const routes = [
  {
    path: "/",
    name: ROUTES_HOME,
    component: HomeView,
    meta: {},
  },
  {
    path: "/getstarted",
    name: ROUTES_GETSTARTED,
    component: GetStartedView,
  },
  {
    path: "/notizenansicht",
    name: ROUTES_NOTIZENANSICHT,
    component: Notizenansicht,
  },
  {
    path: "/persoenliche-todos",
    name: ROUTES_PERSOENLICHE_TODOS, // Konstanten für die Route hinzufügen
    component: PersoenlicheTodos,
  },
  {
    path: "/mir-zugewiesene-todos",
    name: ROUTES_MIR_ZUGEWIESENE_TODOS, // Konstanten für die Route hinzufügen
    component: MirZugewieseneTodos,
  },
  { path: "/:catchAll(.*)*", redirect: "/" }, // CatchAll route
];

// Erstelle den Router
const router = createRouter({
  history: createWebHashHistory(),
  routes,
  scrollBehavior() {
    return {
      top: 0,
      left: 0,
    };
  },
});

// Exportiere den Router
export default router;
