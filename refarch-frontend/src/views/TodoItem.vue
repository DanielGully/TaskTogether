<template>
  <div class="todo-item d-flex align-center mb-0">
    <v-text-field
      v-model="localTodo.text"
      :placeholder="placeholder"
      dense
      hide-details
      clearable
    ></v-text-field>
    <v-btn
      class="delete-button"
      @click="deleteTodo"
      icon
    >
      <v-icon>mdi-delete</v-icon>
    </v-btn>
  </div>
</template>

<script lang="ts">
export default {
  props: {
    todo: Object,
    index: Number,
    deleteCallback: Function,
    placeholder: String,
  },
  data() {
    return {
      localTodo: { ...this.todo },
    };
  },
  methods: {
    deleteTodo() {
      this.deleteCallback(this.index);
    },
  },
  watch: {
    todo: {
      handler(newValue) {
        this.localTodo = { ...newValue };
      },
      deep: true,
    },
  },
};
</script>

<style scoped>
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
