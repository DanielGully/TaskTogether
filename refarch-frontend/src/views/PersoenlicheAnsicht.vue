<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <h2 class="d-flex justify-between align-center">
          Deine persönlichen ToDos:
          <v-btn
            class="ml-auto"
            @click="openModal"
            >ToDo hinzufügen</v-btn
          >
        </h2>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="4">
        <v-card outlined>
          <v-card-title>Abgelaufene Deadlines</v-card-title>
          <div class="todo-list">
            <todo-item
              v-for="(todo, index) in expiredDeadlines"
              :key="index"
              :todo="todo"
              :index="index"
              :deleteCallback="deleteExpiredDeadline"
              placeholder="ToDo"
            />
          </div>
        </v-card>
      </v-col>

      <v-col cols="4">
        <v-card outlined>
          <v-card-title
            >Nächste 2 Wochen (oder laufende Deadlines)</v-card-title
          >
          <div class="todo-list">
            <todo-item
              v-for="(todo, index) in nextTwoWeeks"
              :key="index"
              :todo="todo"
              :index="index"
              :deleteCallback="deleteNextTwoWeek"
              placeholder="ToDo"
            />
          </div>
        </v-card>
      </v-col>

      <v-col cols="4">
        <v-card outlined>
          <v-card-title>Längere Deadlines</v-card-title>
          <div class="todo-list">
            <todo-item
              v-for="(todo, index) in longerDeadlines"
              :key="index"
              :todo="todo"
              :index="index"
              :deleteCallback="deleteLongerDeadline"
              placeholder="ToDo"
            />
          </div>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog
      v-model="modal"
      max-width="600px"
    >
      <v-card>
        <v-card-title>
          <span class="headline">ToDo hinzufügen</span>
        </v-card-title>
        <v-card-text>
          <v-text-field
            v-model="newTodo.title"
            label="*Titel"
            required
            :rules="[(value) => !!value || 'Titel ist erforderlich.']"
          >
          </v-text-field>
          <v-textarea
            v-model="newTodo.description"
            label="Beschreibung"
          ></v-textarea>
          <v-select
            v-model="newTodo.priority"
            :items="['Hoch', 'Mittel', 'Niedrig']"
            label="*Priorität"
            :rules="[(value) => !!value || 'Bitte wähle eine Priorität.']"
            required
          >
          </v-select>
          <v-text-field
            v-model="newTodo.deadline"
            label="Fälligkeitsdatum (TT.MM.JJJJ)"
            :rules="[dateRuleForOptionalField]"
            placeholder="z.B. 31.12.2024"
            mask="##.##.####"
          ></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-btn
            text
            @click="closeModal"
            >Abbrechen</v-btn
          >
          <v-btn
            text
            @click="saveTodo"
            :disabled="!isFormValid"
          >
            Speichern
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import TodoItem from "./TodoItem.vue";

export default {
  components: {
    TodoItem, // Registriere die Komponente
  },
  data() {
    return {
      modal: false,
      newTodo: {
        title: "",
        description: "",
        priority: "Niedrig",
        deadline: "",
      },
      dateRuleForOptionalField: (value) => {
        if (!value) return true;
        const regex =
          /^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[0-2])\.(20[0-9]{2}|19[0-9]{2})$/;
        return (
          regex.test(value) ||
          "Ungültiges Datum. Bitte im Format TT.MM.JJJJ eingeben."
        );
      },
      expiredDeadlines: [],
      nextTwoWeeks: [],
      longerDeadlines: [],
    };
  },
  computed: {
    isFormValid() {
      const isTitleValid = !!this.newTodo.title;
      const isPriorityValid = !!this.newTodo.priority;
      const isDeadlineValid =
        this.dateRuleForOptionalField(this.newTodo.deadline) === true;

      return (
        isTitleValid &&
        isPriorityValid &&
        (this.newTodo.deadline === "" || isDeadlineValid)
      );
    },
  },
  methods: {
    deleteExpiredDeadline(index) {
      this.expiredDeadlines.splice(index, 1);
    },
    deleteNextTwoWeek(index) {
      this.nextTwoWeeks.splice(index, 1);
    },
    deleteLongerDeadline(index) {
      this.longerDeadlines.splice(index, 1);
    },
    openModal() {
      this.modal = true;
    },
    closeModal() {
      this.modal = false;
      this.resetNewTodo();
    },
    saveTodo() {
      const currentDate = new Date();
      const deadlineDate = this.newTodo.deadline
        ? this.parseDate(this.newTodo.deadline)
        : null;

      if (deadlineDate) {
        if (deadlineDate < currentDate) {
          this.expiredDeadlines.push({
            text: this.newTodo.title,
            priority: this.newTodo.priority,
          });
          this.expiredDeadlines = this.sortTodos(this.expiredDeadlines);
        } else if (
          deadlineDate.getMonth() === currentDate.getMonth() &&
          deadlineDate.getFullYear() === currentDate.getFullYear()
        ) {
          this.nextTwoWeeks.push({
            text: this.newTodo.title,
            priority: this.newTodo.priority,
          });
          this.nextTwoWeeks = this.sortTodos(this.nextTwoWeeks);
        } else {
          this.longerDeadlines.push({
            text: this.newTodo.title,
            priority: this.newTodo.priority,
          });
          this.longerDeadlines = this.sortTodos(this.longerDeadlines);
        }
      } else {
        this.longerDeadlines.push({
          text: this.newTodo.title,
          priority: this.newTodo.priority,
        });
        this.longerDeadlines = this.sortTodos(this.longerDeadlines);
      }

      this.closeModal();
    },
    parseDate(dateString) {
      const parts = dateString.split(".");
      return new Date(parts[2], parts[1] - 1, parts[0]);
    },
    resetNewTodo() {
      this.newTodo = {
        title: "",
        description: "",
        priority: "Niedrig",
        deadline: "",
      };
    },
    sortTodos(todos) {
      return todos.sort((a, b) => {
        const priorityOrder = ["Hoch", "Mittel", "Niedrig"];
        const priorityComparison =
          priorityOrder.indexOf(a.priority) - priorityOrder.indexOf(b.priority);
        return priorityComparison !== 0
          ? priorityComparison
          : a.text.localeCompare(b.text);
      });
    },
  },
};
</script>

<style scoped>
.todo-list {
  max-height: 600px;
  overflow-y: auto;
  margin-top: 16px;
}
</style>
