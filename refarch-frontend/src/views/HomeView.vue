<template>
  <v-container>
    <v-row class="text-center">
      <v-col cols="12">
        <v-img
            src="@/assets/logo.png"
            class="my-3"
            height="200"
        />
      </v-col>

      <v-col class="mb-4">
        <h1 class="text-h3 font-weight-bold mb-3">
          {{ "Willkommen bei TaskTogether von it@M" }}
        </h1>
        <p>
          {{ "Das API-Gateway ist:" }}
          <span :class="status">{{ status }}</span>
        </p>
      </v-col>

      <v-col cols="12">
        <v-btn
            color="primary"
            @click="goToNotes"
        >
          Jetzt TODOs ansehen
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user.ts";
import { checkHealth } from "@/api/health-client";
import { useSnackbarStore } from "@/stores/snackbar";
import HealthState from "@/types/HealthState";

const snackbarStore = useSnackbarStore();
const status = ref("DOWN");
const router = useRouter();
const userStore = useUserStore();

onMounted(() => {
  checkHealth()
      .then((content: HealthState) => (status.value = content.status))
      .catch((error) => {
        snackbarStore.showMessage(error);
      });
  tryLoadUserRole();
});

const tryLoadUserRole = () => {
  const userId = userStore.getUser?.sub;

  if (!userId) {
    console.debug("User ID ist nicht vorhanden, versuche es erneut...");
    setTimeout(tryLoadUserRole, 500);
  } else {
    console.debug("Benutzer ID gefunden:", userId);
    const userRoles = userStore.getUser.user_roles;
    console.debug("Benutzerrollen:", userRoles);
  }
};

const goToNotes = () => {
  const userRoles = userStore.getUser?.authorities;

  if (userRoles && userRoles.includes("ROLE_admin")) {
    router.push("/adminview");
  } else {
    router.push("/notizenansicht");
  }
};
</script>
