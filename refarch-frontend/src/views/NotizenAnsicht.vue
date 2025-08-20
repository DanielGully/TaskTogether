<template>
  <v-container>
    <v-row>
      <v-col cols="6">
        <v-card>
          <v-card-title class="d-flex justify-between align-center">
            <span>Persönliche ToDos</span>
            <router-link to="/persoenliche-todos" class="ml-auto">
              <v-btn>...</v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn @click="addTodo">ToDo hinzufügen</v-btn>
          </v-card-actions>
          <div class="todo-list">
            <div
                v-for="(todo, index) in personalTodos"
                :key="index"
                class="todo-item"
                :style="getTodoStyle(todo)"
                @click="logTodoId(todo.id)"
            >
              <span class="todo-title">{{ todo.title }}</span>
              <span
                  class="todo-priority"
                  :style="getPriorityStyle(todo.priority)"
              >Priorität: {{ todo.priority }}</span>
              <span class="todo-deadline">Deadline: {{ formatDateForDisplay(todo.deadlineDatum) }}</span>
              <v-btn @click.stop.prevent="deleteTodo(todo.id)" icon>
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </div>
          </div>
        </v-card>
      </v-col>

      <v-col cols="6">
        <v-card>
          <v-card-title class="d-flex justify-between align-center">
            <span>Mir zugewiesene GruppenToDos</span>
            <router-link to="/mir-zugewiesene-todos" class="ml-auto">
              <v-btn>...</v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn @click="addGroupTodo">ToDo hinzufügen</v-btn>
          </v-card-actions>
          <div class="todo-list">
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import {fetchDeleteToDo, fetchToDosByDeadline} from "@/api/fetch-todos.ts";
import type { Todo } from "@/interfaces";

export default {
  data() {
    return {
      personalTodos: [] as Todo[],
      groupTodos: [] as Todo[],
    };
  },
  mounted() {
    this.loadPersonalTodos();
  },
  methods: {
    loadPersonalTodos() {
      fetchToDosByDeadline()
          .then((todos) => {
            this.personalTodos = todos;
          })
          .catch((err) => {
            console.debug("Fehler beim Abrufen der persönlichen ToDos:", err);
          });
    },
    addGroupTodo() {
      this.groupTodos.push({ text: "", priority: "Niedrig" });
    },
    deleteTodo(todoId) {
      console.debug(todoId);
      fetchDeleteToDo(todoId)
          .then(() => {
            this.loadPersonalTodos();
          })
          .catch((err) => console.debug("Fehler beim Löschen:", err));

    },
    getTodoStyle(todo) {
      const today = new Date();
      const deadline = todo.deadlineDatum ? new Date(todo.deadlineDatum) : null;

      if (!deadline) {
        return { 'border-left': '8px solid green' };
      } else if (deadline < today) {
        return { 'border-left': '8px solid red' };
      } else if (deadline.getMonth() === today.getMonth() && deadline.getFullYear() === today.getFullYear()) {
        return { 'border-left': '8px solid #FFA500' };
      } else {
        return { 'border-left': '8px solid green' };
      }
    },
    getPriorityStyle(priority: string) {
      if (priority === "HOCH") {
        return { color: 'red', fontSize: 'initial' };
      } else if (priority === "MITTEL") {
        return { color: '#FFA500', fontSize: 'initial' };
      } else {
        return { color: 'green', fontSize: 'initial' };
      }
    },
    formatDateForDisplay(dateString) {
      if (!dateString || dateString === "keine" || dateString === "ungültiges Datum") {
        return "keine";
      }
      const parts = dateString.split('-');
      return `${parts[2]}.${parts[1]}.${parts[0]}`;
    },
    logTodoId(todoId) {
      console.debug("Klicke auf ToDo mit ID:", todoId);
    }
  },
};
</script>

<style scoped>
.todo-list {
  max-height: 600px;
  overflow-y: auto;
  margin-top: 16px;
}
.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  transition: background-color 0.3s, transform 0.1s;
}
.todo-item:hover {
  background-color: #f0f0f0;
  cursor: pointer;
}
.todo-title {
  flex: 2;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.todo-priority,
.todo-deadline {
  flex: 1;
  margin-left: 10px;
  font-style: italic;
  color: #666;
}
</style>
