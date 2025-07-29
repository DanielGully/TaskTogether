<template>
  <v-container>
    <v-row>
      <v-col cols="6">
        <v-card>
          <v-card-title class="d-flex justify-between align-center">
            <span>Persönliche ToDos</span>
            <router-link to="/persoenliche-todos" class="ml-auto">
              <v-btn>
                ...
              </v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn @click="addTodo">ToDo hinzufügen</v-btn>
          </v-card-actions>
          <div class="todo-list">
            <div
                v-for="(todo, index) in personalTodos"
                :key="index"
                class="todo-item d-flex align-center mb-0"
            >
              <v-text-field
                  v-model="todo.text"
                  placeholder="Neues ToDo"
                  dense
                  hide-details
                  clearable
              ></v-text-field>
              <v-btn
                  class="delete-button"
                  @click="deleteTodo(index)"
                  icon
              >
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
              <v-btn>
                ...
              </v-btn>
            </router-link>
          </v-card-title>
          <v-card-actions>
            <v-btn @click="addGroupTodo">ToDo hinzufügen</v-btn>
          </v-card-actions>
          <div class="todo-list">
            <div
                v-for="(todo, index) in groupTodos"
                :key="index"
                class="todo-item d-flex align-center mb-0"
            >
              <v-text-field
                  v-model="todo.text"
                  placeholder="Neues GruppenToDo"
                  dense
                  hide-details
                  clearable
              ></v-text-field>
              <v-btn
                  class="delete-button"
                  @click="deleteGroupTodo(index)"
                  icon
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </div>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      personalTodos: [],
      groupTodos: []
    };
  },
  methods: {
    addTodo() {
      this.personalTodos.push({ text: '' });
    },
    addGroupTodo() {
      this.groupTodos.push({ text: '' });
    },
    deleteTodo(index) {
      this.personalTodos.splice(index, 1);
    },
    deleteGroupTodo(index) {
      this.groupTodos.splice(index, 1);
    }
  }
}
</script>

<style scoped>
.todo-list {
  max-height: 453px;
  overflow-y: auto;
  margin-top: 16px;
}

.todo-item {
  display: flex;
  align-items: center;
  margin: 0;
  width: 100%;
}

.delete-button {
  align-items: center;
}
</style>
