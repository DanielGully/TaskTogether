<template>
  <div class="container">
    <h1>Deine persönlichen ToDos</h1>
    <button @click="openModal">ToDo hinzufügen</button>

    <div class="todo-sections">
      <div class="todo-section">
        <h2>Hohe Priorität</h2>
        <div class="todo-list">
          <div
              v-for="(todo, index) in highPriorityTodos"
              :key="index"
              class="todo-item"
              :style="{'background-color': getBackgroundColor(todo)}"
          >
            <h3>{{ todo.title }}</h3>
            <p>{{ todo.description }}</p>
            <span>Deadline: {{ formatDate(todo.deadlineDatum) }}</span>
          </div>
        </div>
      </div>

      <div class="todo-section">
        <h2>Mittlere Priorität</h2>
        <div class="todo-list">
          <div
              v-for="(todo, index) in mediumPriorityTodos"
              :key="index"
              class="todo-item"
              :style="{'background-color': getBackgroundColor(todo)}"
          >
            <h3>{{ todo.title }}</h3>
            <p>{{ todo.description }}</p>
            <span>Deadline: {{ formatDate(todo.deadlineDatum) }}</span>
          </div>
        </div>
      </div>

      <div class="todo-section">
        <h2>Niedrige Priorität</h2>
        <div class="todo-list">
          <div
              v-for="(todo, index) in lowPriorityTodos"
              :key="index"
              class="todo-item"
              :style="{'background-color': getBackgroundColor(todo)}"
          >
            <h3>{{ todo.title }}</h3>
            <p>{{ todo.description }}</p>
            <span>Deadline: {{ formatDate(todo.deadlineDatum) }}</span>
          </div>
        </div>
      </div>
    </div>

    <modal v-if="modal" @close="closeModal">
      <template #header>
        <h3>ToDo hinzufügen</h3>
      </template>
      <template #body>
        <input v-model="newTodo.title" placeholder="Titel" required />
        <textarea v-model="newTodo.description" placeholder="Beschreibung"></textarea>
        <select v-model="newTodo.priority" required>
          <option value="Hoch">Hoch</option>
          <option value="Mittel">Mittel</option>
          <option value="Niedrig">Niedrig</option>
        </select>
        <input v-model="newTodo.deadlineDatum" placeholder="Fälligkeitsdatum (TT.MM.JJJJ)" />
      </template>
      <template #footer>
        <button @click="saveTodo">Speichern</button>
        <button @click="closeModal">Abbrechen</button>
      </template>
    </modal>
  </div>
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
    getBackgroundColor(todo: ToDoResponseDTO) {
      const currentDate = new Date();
      if (!todo.deadlineDatum) {
        return "green";
      }
      const deadlineDate = new Date(todo.deadlineDatum);
      if (deadlineDate < currentDate) {
        return "red";
      } else if (deadlineDate.getMonth() === currentDate.getMonth() &&
          deadlineDate.getFullYear() === currentDate.getFullYear()) {
        return "yellow";
      }
      return "green";
    },
    formatDate(dateString: string) {
      if (!dateString) {
        return "keine";
      }
      const date = new Date(dateString);
      return date.toLocaleDateString("de-DE");
    },
  },
};
</script>

<style scoped>
.container {
  padding: 20px;
}
.todo-sections {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
}
.todo-section {
  margin: 15px 0;
}
.todo-list {
  display: flex;
  flex-direction: column;
}
.todo-item {
  padding: 10px;
  border-radius: 5px;
  margin: 5px 0;
}
button {
  margin: 10px;
}
</style>
