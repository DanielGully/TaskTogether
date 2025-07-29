<template>
  <v-container>
    <v-row>
      <v-col cols="6">
        <v-card class="d-flex flex-column justify-between h-100">
          <v-card-title class="d-flex justify-between align-center">
            <span>Persönliche ToDos</span>
            <router-link to="/persoenliche-todos" class="ml-auto">
              <v-btn>
                ...
              </v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn class="ml-0">ToDo hinzufügen</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>

      <v-col cols="6">
        <v-card class="d-flex flex-column justify-between h-100">
          <v-card-title class="d-flex justify-between align-center">
            <span>Mir zugewiesene GruppenToDos</span>
            <router-link to="/mir-zugewiesene-todos" class="ml-auto">
              <v-btn>
                ...
              </v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn class="ml-0">ToDo hinzufügen</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
