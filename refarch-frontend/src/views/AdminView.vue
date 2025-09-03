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

onMounted(() => {
  loadTodos();
});
</script>

<template>
  <div>
    <h1>ToDos für Admin</h1>
    <div v-if="error" class="error">{{ error }}</div>
    <ul v-else>
      <li v-for="todo in todos" :key="todo.id">{{ todo.title }}</li>
    </ul>
  </div>
</template>

<style scoped>
.error {
  color: red;
}
</style>
