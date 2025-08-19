<template>
  <v-container>
    <div style="display: flex; justify-content: space-between; align-items: center;">
      <h1>Deine persönlichen ToDos</h1>
      <v-btn @click="addTodo" class="add-todo-button">ToDo hinzufügen</v-btn>
    </div>

    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            <span>Hohe Priorität</span>
          </v-card-title>
          <v-card-text>
            <div class="todo-list">
              <div
                  v-for="(todo, index) in highPriorityTodos"
                  :key="index"
                  class="todo-item"
                  :style="{'border-left': '4px solid red'}"
                  @click="logTodoDetails(todo)"
              >
                <span class="todo-title">{{ todo.title }}</span>
                <span class="todo-description">{{ todo.description }}</span>
                <span class="todo-priority">Priorität: {{ todo.priority }}</span>
                <span class="todo-deadline">Deadline: {{ formatDate(todo.deadlineDatum) }}</span>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12">
        <v-card>
          <v-card-title>
            <span>Mittlere Priorität</span>
          </v-card-title>
          <v-card-text>
            <div class="todo-list">
              <div
                  v-for="(todo, index) in mediumPriorityTodos"
                  :key="index"
                  class="todo-item"
                  :style="{'border-left': '4px solid orange'}"
                  @click="logTodoDetails(todo)"
              >
                <span class="todo-title">{{ todo.title }}</span>
                <span class="todo-description">{{ todo.description }}</span>
                <span class="todo-priority">Priorität: {{ todo.priority }}</span>
                <span class="todo-deadline">Deadline: {{ formatDate(todo.deadlineDatum) }}</span>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12">
        <v-card>
          <v-card-title>
            <span>Niedrige Priorität</span>
          </v-card-title>
          <v-card-text>
            <div class="todo-list">
              <div
                  v-for="(todo, index) in lowPriorityTodos"
                  :key="index"
                  class="todo-item"
                  :style="{'border-left': '4px solid green'}"
                  @click="logTodoDetails(todo)"
              >
                <span class="todo-title">{{ todo.title }}</span>
                <span class="todo-description">{{ todo.description }}</span>
                <span class="todo-priority">Priorität: {{ todo.priority }}</span>
                <span class="todo-deadline">Deadline: {{ formatDate(todo.deadlineDatum) }}</span>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <modal v-if="modal" @close="closeModal">
      <template #header>
        <h3>ToDo hinzufügen</h3>
      </template>
      <template #body>
        <v-text-field v-model="newTodo.title" placeholder="Titel" required />
        <v-textarea v-model="newTodo.description" placeholder="Beschreibung"></v-textarea>
        <v-select
            v-model="newTodo.priority"
            :items="['Hoch', 'Mittel', 'Niedrig']"
            required
            label="Priorität"
        />
        <v-text-field v-model="newTodo.deadlineDatum" placeholder="Fälligkeitsdatum (TT.MM.JJJJ)" />
      </template>
      <template #footer>
        <v-btn @click="saveTodo">Speichern</v-btn>
        <v-btn @click="closeModal">Abbrechen</v-btn>
      </template>
    </modal>
  </v-container>
</template>

<script lang="ts">
import { fetchToDosByPriority } from "@/api/fetch-todos.ts";
import type { ToDoResponseDTO } from "@/types/ToDo.ts";

export default {
  data() {
    return {
      modal: false,
      newTodo: {
        title: "",
        description: "",
        priority: "Niedrig",
        deadlineDatum: "",
      } as {
        title: string;
        description: string;
        priority: "Hoch" | "Mittel" | "Niedrig";
        deadlineDatum: string;
      },
      highPriorityTodos: [] as ToDoResponseDTO[],
      mediumPriorityTodos: [] as ToDoResponseDTO[],
      lowPriorityTodos: [] as ToDoResponseDTO[],
    };
  },
  mounted() {
    this.loadTodos();
  },
  methods: {
    loadTodos() {
      fetchToDosByPriority("HOCH").then(todos => {
        this.highPriorityTodos = todos;
      });

      fetchToDosByPriority("MITTEL").then(todos => {
        this.mediumPriorityTodos = todos;
      });

      fetchToDosByPriority("NIEDRIG").then(todos => {
        this.lowPriorityTodos = todos;
      });
    },
    openModal() {
      this.modal = true;
    },
    closeModal() {
      this.modal = false;
      this.resetNewTodo();
    },
    saveTodo() {
      const todo: ToDoResponseDTO = {
        title: this.newTodo.title,
        description: this.newTodo.description,
        priority: this.newTodo.priority,
        deadlineDatum: this.newTodo.deadlineDatum,
      };

      if (todo.deadlineDatum === "") {
        todo.deadlineDatum = null;
      }

      if (todo.priority === "Hoch") {
        this.highPriorityTodos.push(todo);
      } else if (todo.priority === "Mittel") {
        this.mediumPriorityTodos.push(todo);
      } else {
        this.lowPriorityTodos.push(todo);
      }

      this.closeModal();
    },
    resetNewTodo() {
      this.newTodo = {
        title: "",
        description: "",
        priority: "Niedrig",
        deadlineDatum: "",
      };
    },
    formatDate(dateString: string) {
      if (!dateString) {
        return "keine";
      }
      const date = new Date(dateString);
      return date.toLocaleDateString("de-DE");
    },
    logTodoDetails(todo) {
      console.debug("Titel:", todo.title);
      console.debug("Beschreibung:", todo.description);
      console.debug("Priorität:", todo.priority);
      console.debug("Deadline:", todo.deadlineDatum);
    },
    addTodo() { // Neue Methode für den Button
      console.debug("ToDo hinzufügen"); // Konsolenausgabe
      this.openModal(); // Modal öffnen
    }
  },
};
</script>

<style scoped>
.todo-title,
.todo-description,
.todo-priority,
.todo-deadline {
  font-size: 1.2em;
}
.add-todo-button {
  margin-bottom: 20px;
  transition: background-color 0.3s, transform 0.1s; /* Für Übergänge */
}

.add-todo-button:hover {
  background-color: #f0f0f0; /* Hintergrundfarbe bei Hover */
  cursor: pointer; /* Zeigeränderung bei Hover */
}

.add-todo-button:active {
  transform: scale(0.98); /* Leicht verkleinern beim Klicken */
}

.todo-list {
  max-height: 400px;
  overflow-y: auto;
}
.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 1px solid #ccc; /* Umrandung */
  border-radius: 4px; /* Runde Ecken */
  transition: background-color 0.3s, transform 0.1s; /* Für Übergänge */
}

.todo-item:hover {
  background-color: #f0f0f0; /* Hintergrundfarbe bei Hover */
  cursor: pointer; /* Zeigeränderung bei Hover */
}

.todo-item:active {
  transform: scale(0.98); /* Leicht verkleinern beim Klicken */
}

.todo-title {
  flex: 3;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.todo-description {
  flex: 4;
  margin: 0 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.todo-priority,
.todo-deadline {
  flex: 2;
  margin-left: 10px;
  font-style: italic;
  color: #666;
}
.todo-deadline {
  font-style: italic;
  color: #666;
}
</style>
