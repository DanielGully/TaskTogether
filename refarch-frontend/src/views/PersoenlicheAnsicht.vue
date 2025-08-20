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
                  @click="logTodoId(todo.id); editTodo(todo)"
              >
                <span class="todo-title">{{ todo.title }}</span>
                <span class="todo-description">{{ todo.description }}</span>
                <span class="todo-priority">Priorität: {{ todo.priority }}</span>
                <span class="todo-deadline">Deadline: {{ formatDateForDisplay(todo.deadlineDatum) }}</span>
                <v-btn @click.stop.prevent="deleteTodo(todo.id)" icon>
                  <!-- Hier später das Icon einfügen -->
                </v-btn>
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
import { fetchToDosByPriority, fetchCreateToDo, fetchUpdateToDo, fetchDeleteToDo } from "@/api/fetch-todos.ts";
import type { ToDoResponseDTO } from "@/types/ToDo.ts";

export default {
  data() {
    return {
      modal: false,
      isUpdate: false,
      selectedTodoId: null,
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

    deleteTodo(todoId) {
      console.debug(todoId);
        fetchDeleteToDo(todoId)
            .then(() => {
              this.loadTodos();
            })
            .catch((err) => console.debug("Fehler beim Löschen:", err));

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
    },

    saveTodo() {
      const todoData = {
        title: this.newTodo.title,
        description: this.newTodo.description,
        priority: this.newTodo.priority.toUpperCase(),
        deadlineDatum: this.formatDate(this.newTodo.deadlineDatum),
      };

      if (this.isUpdate) {
        fetchUpdateToDo(this.selectedTodoId, { ...todoData })
            .then(() => {
              this.loadTodos();
            })
            .catch((err) => console.debug("Fehler beim Aktualisieren:", err));
      } else {
        fetchCreateToDo({ id: "97b0690c-d647-4958-bf2f-cf18d84dc59d", ...todoData })
            .then(() => {
              this.loadTodos();
            })
            .catch((err) => console.debug("Fehler beim Erstellen:", err));
      }

      this.newTodo = {
        title: "",
        description: "",
        priority: "Niedrig",
        deadlineDatum: "",
      };
      this.selectedTodoId = null;
      this.closeModal();
    },

    formatDate(dateString: string) {
      if (!dateString) {
        return null;
      }

      const parts = dateString.split('.');
      if (parts.length !== 3) {
        return "ungültiges Datum";
      }

      const day = parts[0].padStart(2, '0');
      const month = parts[1].padStart(2, '0');
      const year = parts[2];

      if (isNaN(Number(day)) || isNaN(Number(month)) || isNaN(Number(year))) {
        return "ungültiges Datum";
      }

      return `${year}-${month}-${day}`;
    },

    editTodo(todo) {
      this.selectedTodoId = todo.id;
      this.newTodo.title = todo.title;
      this.newTodo.description = todo.description;
      this.newTodo.priority = todo.priority;
      this.newTodo.deadlineDatum = todo.deadlineDatum ? this.formatDateForDisplay(todo.deadlineDatum) : "";
      this.isUpdate = true;
      this.modal = true;
    },

    addTodo() {
      this.isUpdate = false;
      this.modal = true;
    },

    formatDateForDisplay(dateString: string) {
      if (!dateString || dateString === "keine" || dateString === "ungültiges Datum") {
        return "keine";
      }

      const parts = dateString.split('-');
      if (parts.length !== 3) {
        return "ungültiges Datum";
      }

      const year = parts[0];
      const month = parts[1].padStart(2, '0');
      const day = parts[2].padStart(2, '0');

      return `${day}.${month}.${year}`;
    },

    checkDateFormat(dateString) {
      const regex = /^\d{2}\.\d{2}\.\d{4}$/;
      return regex.test(dateString);
    },

    logTodoId(todoId) {
      console.debug("Klicke auf ToDo mit ID:", todoId);
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
