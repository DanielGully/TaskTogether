<template>
  <div class="todo-item d-flex align-center mb-0">
    <v-text-field
        v-model="todo.text"
        :placeholder="placeholder"
        dense
        hide-details
        clearable
    ></v-text-field>
    <v-btn class="delete-button" @click="deleteTodo" icon>
      <v-icon>mdi-delete</v-icon>
    </v-btn>
  </div>
</template>

<script>
export default {
  props: {
    todo: Object,
    index: Number,
    deleteCallback: Function,
    placeholder: String
  },
  methods: {
    deleteTodo() {
      this.deleteCallback(this.index);
    }
  }
}
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
