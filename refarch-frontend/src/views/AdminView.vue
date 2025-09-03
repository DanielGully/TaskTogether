<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { fetchAllToDosForAdmin } from '@/api/fetch-todos.ts';

const todos = ref([]);
const error = ref(null);

const loadTodos = async () => {
  try {
    todos.value = await fetchAllToDosForAdmin();
  } catch (err) {
    error.value = 'Fehler beim Laden der ToDos: ' + err.message;
  }
};

const formatDateForDisplay = (dateString) => {
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
};

const getTodoStyle = (todo) => {
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
};

onMounted(() => {
  loadTodos();
});
</script>

<template>
  <v-container>
    <div style="display: flex; justify-content: space-between; align-items: center;">
      <h1>Admin-Ansicht</h1>
    </div>

    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-text>
            <div class="todo-list">
              <div
                  v-for="todo in todos"
                  :key="todo.id"
                  class="todo-item"
                  :style="getTodoStyle(todo)"
              >
                <span class="todo-title">{{ todo.title }}</span>
                <span class="todo-description">{{ todo.description }}</span>
                <span class="todo-priority">Priorität: {{ todo.priority }}</span>
                <span class="todo-deadline">Deadline: {{ formatDateForDisplay(todo.deadlineDatum) }}</span>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.todo-list {
  max-height: 400px;
  overflow-y: auto;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
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
  font-size: 1.5em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.todo-description {
  flex: 4;
  margin: 0 10px;
  font-size: 1.2em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.todo-priority,
.todo-deadline {
  flex: 2;
  margin-left: 10px;
  font-size: 1.2em;
  font-style: italic;
  color: #666;
}

.todo-deadline {
  font-style: italic;
  color: #666;
}
</style>

