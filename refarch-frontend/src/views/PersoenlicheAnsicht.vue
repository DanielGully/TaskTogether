<template>
  <v-container>
    <div style="display: flex; justify-content: space-between; align-items: center;">
      <h1>Deine persönlichen ToDos</h1>
      <v-btn @click="addTodo" class="add-todo-button">ToDo hinzufügen</v-btn>
    </div>

    <v-row>
      <v-col cols="12" v-for="(priority, index) in priorities" :key="index">
        <v-card>
          <v-card-title>
            <span>{{ priority.label }}</span>
          </v-card-title>
          <v-card-text>
            <div class="todo-list">
              <div
                  v-for="(todo, index) in priority.todos"
                  :key="index"
                  class="todo-item"
                  :style="getTodoStyle(todo)"
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

    <v-dialog v-model="modal" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">ToDo hinzufügen</span>
        </v-card-title>
        <v-card-text>
          <v-text-field
              v-model="newTodo.title"
              placeholder="Titel"
              :rules="[v => !!v || 'Titel ist erforderlich']"
              required
          />
          <v-text-field
              v-model="newTodo.description"
              placeholder="Beschreibung"
              type="textarea"
              :style="{ resize: 'none', minHeight: '200px' }"
              rows="5"
          ></v-text-field>
          <v-select
              v-model="newTodo.priority"
              :items="prioritiesOptions"
              required
              label="Priorität"
          />
          <v-text-field
              v-model="newTodo.deadlineDatum"
              :placeholder="'Fälligkeitsdatum (TT.MM.JJJJ)'"
              :rules="[v => !v || checkDateFormat(v) || 'Ungültiges Datum']"
          />
          <div>
            <span v-if="!newTodo.title" style="color: red;">Titel ist erforderlich.</span>
            <span v-if="newTodo.deadlineDatum && !checkDateFormat(newTodo.deadlineDatum)" style="color: red;">Ungültiges Datum.</span>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn @click="saveTodo" :disabled="!canSave">Speichern</v-btn>
          <v-btn @click="closeModal">Abbrechen</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
import { fetchToDosByPriority } from "@/api/fetch-todos.ts";
import type { ToDoResponseDTO } from "@/types/ToDo.ts";

export default {
  data() {
    return {
      modal: false,
      isUpdate: false, // Neues Datenattribut
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
      priorities: [
        { label: 'Hohe Priorität', todos: [] },
        { label: 'Mittlere Priorität', todos: [] },
        { label: 'Niedrige Priorität', todos: [] }
      ],
      prioritiesOptions: ['Hoch', 'Mittel', 'Niedrig'],
    };
  },
  computed: {
    canSave() {
      return this.newTodo.title && (this.newTodo.deadlineDatum === "" || this.checkDateFormat(this.newTodo.deadlineDatum));
    }
  },
  mounted() {
    this.loadTodos();
  },
  methods: {
    loadTodos() {
      fetchToDosByPriority("HOCH").then((todos: ToDoResponseDTO[]) => {
        this.priorities[0].todos = todos;
      });

      fetchToDosByPriority("MITTEL").then((todos: ToDoResponseDTO[]) => {
        this.priorities[1].todos = todos;
      });

      fetchToDosByPriority("NIEDRIG").then((todos: ToDoResponseDTO[]) => {
        this.priorities[2].todos = todos;
      });
    },
    getTodoStyle(todo) {
      const today = new Date();
      const deadline = todo.deadlineDatum ? new Date(todo.deadlineDatum) : null;

      if (!deadline) {
        return {'border-left': '4px solid green'};
      } else if (deadline < today) {
        return {'border-left': '4px solid red'};
      } else if (deadline.getMonth() === today.getMonth() && deadline.getFullYear() === today.getFullYear()) {
        return {'border-left': '4px solid yellow'};
      } else {
        return {'border-left': '4px solid green'};
      }
    },
    closeModal() {
      this.modal = false;
      // Reset nicht hier aufrufen
    },
    saveTodo() {
      // Hier wird noch nichts gespeichert,
      // modal wird nur geschlossen.
      this.closeModal();
      this.resetNewTodo(); // Nur nach dem Speichern zurücksetzen
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
      const day = String(date.getDate()).padStart(2, '0');  // Sicherstellen, dass der Tag zweistellig ist
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Monat ist 0-indexiert, sicherstellen, dass er zweistellig ist
      const year = date.getFullYear();

      return `${day}.${month}.${year}`; // Format zurückgeben im Format TT.MM.JJJJ
    },
    logTodoDetails(todo) {
      console.debug("ID:", todo.id); // ID in der Konsole ausgeben

      // Fülle die newTodo mit den Daten des gewählten ToDos
      this.newTodo.title = todo.title;
      this.newTodo.description = todo.description;
      this.newTodo.priority = todo.priority;
      this.newTodo.deadlineDatum = this.formatDate(todo.deadlineDatum);

      this.isUpdate = true; // Setze isUpdate auf true
      console.debug("ToDo aktualisieren"); // Loggen, dass wir updaten werden

      // Modal öffnen
      this.modal = true;
    },
    addTodo() {
      console.debug("ToDo hinzufügen");
      this.isUpdate = false; // Setze isUpdate auf false
      this.modal = true;
    },
    checkDateFormat(dateString) {
      const regex = /^\d{2}\.\d{2}\.\d{4}$/; // TT.MM.JJJJ
      return regex.test(dateString);
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
  transition: background-color 0.3s, transform 0.1s;
}

.add-todo-button:hover {
  background-color: #f0f0f0;
  cursor: pointer;
}

.add-todo-button:active {
  transform: scale(0.98);
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
  border: 1px solid #ccc;
  border-radius: 4px;
  transition: background-color 0.3s, transform 0.1s;
}

.todo-item:hover {
  background-color: #f0f0f0;
  cursor: pointer;
}

.todo-item:active {
  transform: scale(0.98);
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
