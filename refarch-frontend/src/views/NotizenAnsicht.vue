<template>
  <v-container>
    <v-row>
      <v-col cols="6">
        <v-card>
          <v-card-title class="d-flex justify-between align-center">
            <span>Persönliche ToDos</span>
            <router-link
              to="/persoenliche-todos"
              class="ml-auto"
            >
              <v-btn>...</v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn @click="addTodo">ToDo hinzufügen</v-btn>
          </v-card-actions>
          <div class="todo-list">
            <todo-item
              v-for="(todo, index) in personalTodos"
              :key="index"
              :todo="todo"
              :index="index"
              :deleteCallback="deleteTodo"
              placeholder="Neues ToDo"
            />
          </div>
        </v-card>
      </v-col>

      <v-col cols="6">
        <v-card>
          <v-card-title class="d-flex justify-between align-center">
            <span>Mir zugewiesene GruppenToDos</span>
            <router-link
              to="/mir-zugewiesene-todos"
              class="ml-auto"
            >
              <v-btn>...</v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn @click="addGroupTodo">ToDo hinzufügen</v-btn>
          </v-card-actions>
          <div class="todo-list">
            <todo-item
              v-for="(todo, index) in groupTodos"
              :key="index"
              :todo="todo"
              :index="index"
              :deleteCallback="deleteGroupTodo"
              placeholder="Neues GruppenToDo"
            />
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import TodoItem from "./TodoItem.vue";

export default {
  components: {
    TodoItem,
  },
  data() {
    return {
      personalTodos: [],
      groupTodos: [],
    };
  },
  methods: {
    addTodo() {
      this.personalTodos.push({ text: "" });
    },
    addGroupTodo() {
      this.groupTodos.push({ text: "" });
    },
    deleteTodo(index) {
      this.personalTodos.splice(index, 1);
    },
    deleteGroupTodo(index) {
      this.groupTodos.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.todo-list {
  max-height: 453px;
  overflow-y: auto;
  margin-top: 16px;
}
</style>
