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
                @click="editTodo(todo)"
            >
              <span class="todo-title">{{ todo.title }}</span>
              <span
                  class="todo-priority"
                  :style="getPriorityStyle(todo.priority)"
              >Priorität: {{ todo.priority }}</span>
              <span class="todo-deadline">Deadline: {{ formatDateForDisplay(todo.deadlineDatum) }}</span>
              <v-btn @click.stop.prevent="deleteTodo(todo.id)" icon>
                <img :src="deleteIcon" alt="Delete" style="width: 24px; height: 24px;" />
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

    <v-dialog v-model="modal" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">{{ isUpdate ? 'ToDo bearbeiten' : 'ToDo hinzufügen' }}</span>
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
              :rules="[ v => !v || checkDateFormat(v) || 'Ungültiges Datum.' ]"
          />
          <div>
            <span v-if="!newTodo.title" style="color: red;">Titel ist erforderlich.</span>
            <span v-if="newTodo.deadlineDatum && !checkDateFormat(newTodo.deadlineDatum)" style="color: red;">Ungültiges Datum.</span>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn @click="closeModal">Abbrechen</v-btn>
          <v-btn @click="saveTodo" :disabled="!isFormValid()">Speichern</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { fetchCreateToDo, fetchDeleteToDo, fetchToDosByDeadline, fetchUpdateToDo } from "@/api/fetch-todos.ts";
import type { Todo } from "@/interfaces";
import {useUserStore} from "@/stores/user.ts";
import deleteIcon from '@/assets/bin.png';


export default defineComponent({
  data() {
    return {
      personalTodos: [] as Todo[],
      groupTodos: [] as Todo[],
      modal: false,
      isUpdate: false,
      selectedTodoId: null,
      newTodo: {
        title: "",
        description: "",
        priority: "Niedrig",
        deadlineDatum: "",
      },
      prioritiesOptions: ['Hoch', 'Mittel', 'Niedrig'],
      deleteIcon: deleteIcon,
    };
  },

  computed: {
    userId() {
      const userStore = useUserStore();
      return userStore.getUser?.sub;
    }
  },

  mounted() {
    const user = useUserStore().getUser;
    if(user != null) {
      console.debug(user.sub);
    } else {
      console.debug("User noch nicht geladen.");
    }
    this.tryLoadPersonalTodos();
  },
  methods: {
    loadPersonalTodos() {
      if (!this.userId) {
        console.debug("User ID ist nicht vorhanden, ToDos können nicht geladen werden.");
        return;
      }
      fetchToDosByDeadline(this.userId)
          .then((todos) => {
            this.personalTodos = todos;
          })
          .catch((err) => {
            console.debug("Fehler beim Abrufen der persönlichen ToDos:", err);
          });
    },
    tryLoadPersonalTodos() {
      if (!this.userId) {
        console.debug("User ID ist nicht vorhanden, versuche es erneut...");
        setTimeout(() => {
          this.tryLoadPersonalTodos();
        }, 500);
      } else {
        this.loadPersonalTodos();
      }
    },
    addGroupTodo() {
      this.groupTodos.push({ text: "", priority: "Niedrig" });
    },
    deleteTodo(todoId) {
      if (!this.userId) {
        console.debug("User ID ist nicht vorhanden, ToDo kann nicht gelöscht werden.");
        return;
      }

      console.debug(todoId);
      fetchDeleteToDo(todoId, this.userId)
          .then(() => {
            this.loadPersonalTodos();
          })
          .catch((err) => console.debug("Fehler beim Löschen:", err));
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
    saveTodo() {
      if (!this.userId) {
        console.debug("User ID ist nicht vorhanden, ToDo kann nicht gespeichert werden.");
        return;
      }

      const todoData = {
        title: this.newTodo.title,
        description: this.newTodo.description,
        priority: this.newTodo.priority.toUpperCase(),
        deadlineDatum: this.formatDate(this.newTodo.deadlineDatum),
      };

      const fetchMethod = this.isUpdate ? fetchUpdateToDo : fetchCreateToDo;

      if (this.isUpdate) {
        fetchMethod(this.selectedTodoId, { ...todoData }, this.userId)
            .then(() => {
              this.loadPersonalTodos();
            })
            .catch((err) => console.debug("Fehler beim Aktualisieren:", err));
      } else {
        fetchMethod({ id: "97b0690c-d647-4958-bf2f-cf18d84dc59d", ...todoData }, this.userId)
            .then(() => {
              this.loadPersonalTodos();
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
    resetNewTodo() {
      this.newTodo = {
        title: "",
        description: "",
        priority: "Niedrig",
        deadlineDatum: "",
      };
      this.selectedTodoId = null;
      this.isUpdate = false;
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
    getPriorityStyle(priority) {
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
    closeModal() {
      this.modal = false;
      this.resetNewTodo();
    },
    checkDateFormat(dateString) {
      const regex = /^\d{2}\.\d{2}\.\d{4}$/;
      return regex.test(dateString);
    },
    isFormValid() {
      return this.newTodo.title && (this.newTodo.deadlineDatum === "" || this.checkDateFormat(this.newTodo.deadlineDatum));
    },
    addTodo() {
      this.resetNewTodo();
      this.modal = true;
    }
  },
});
</script>

<style scoped>
.todo-list {
  max-height: 453px;
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
.todo-item:active {
  transform: scale(0.98);
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
  flex: 2;
  margin-left: 10px;
  font-style: italic;
  color: #666;
}
</style>
