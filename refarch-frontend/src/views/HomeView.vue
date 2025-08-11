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
          {{ "views.home.header" }}
        </h1>
        <p>
          {{ "views.home.apiGatewayStatus" }}
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

import { checkHealth } from "@/api/health-client";
import { useSnackbarStore } from "@/stores/snackbar";
import HealthState from "@/types/HealthState";

const snackbarStore = useSnackbarStore();
const status = ref("DOWN");

onMounted(() => {
  checkHealth()
    .then((content: HealthState) => (status.value = content.status))
    .catch((error) => {
      snackbarStore.showMessage(error);
    });
});

const router = useRouter();
const goToNotes = () => {
  router.push("/notizenansicht");
};
</script>
