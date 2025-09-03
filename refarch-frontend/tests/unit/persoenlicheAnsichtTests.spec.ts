import { shallowMount } from "@vue/test-utils";
import { createPinia, setActivePinia } from "pinia";
import { describe, expect, test, beforeEach, vi } from "vitest";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import PersoenlicheAnsicht from "@/views/PersoenlicheAnsicht.vue";
import { fetchToDosByPriority, fetchCreateToDo, fetchDeleteToDo } from "@/api/fetch-todos.ts";
import { useUserStore } from "@/stores/user.ts";

const pinia = createPinia();
const vuetify = createVuetify({
    components,
    directives,
});

vi.mock('@/api/fetch-todos.ts', () => ({
    fetchToDosByPriority: vi.fn().mockResolvedValue([]),
    fetchCreateToDo: vi.fn().mockResolvedValue({}),
    fetchDeleteToDo: vi.fn().mockResolvedValue({}),
    fetchUpdateToDo: vi.fn().mockResolvedValue({}),
}));

describe("PersoenlicheAnsicht.vue", () => {
    let wrapper;
    let userStore;

    beforeEach(() => {
        setActivePinia(pinia);
        userStore = useUserStore();
        userStore.setUser({ sub: 'mocked-user-id' });

        wrapper = shallowMount(PersoenlicheAnsicht, {
            global: {
                plugins: [pinia, vuetify],
            },
        });
    });

    test("loads todos on mount", async () => {
        const todos = [
            { id: 1, title: 'Test Todo 1', description: 'Description 1', priority: 'Hoch', deadlineDatum: '2023-10-01' },
            { id: 2, title: 'Test Todo 2', description: 'Description 2', priority: 'Mittel', deadlineDatum: '2023-10-02' },
        ];

        fetchToDosByPriority.mockResolvedValueOnce(todos);

        await wrapper.vm.loadTodos();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.priorities[0].todos.length).toBe(2);
        expect(wrapper.vm.priorities[0].todos).toEqual(todos);
    });

    test("adds a new todo", async () => {
        fetchCreateToDo.mockResolvedValueOnce({});

        wrapper.vm.newTodo = {
            title: 'New Todo',
            description: 'New Description',
            priority: 'Niedrig',
            deadlineDatum: '01.10.2023',
        };

        await wrapper.vm.saveTodo();

        expect(fetchCreateToDo).toHaveBeenCalled();
        expect(wrapper.vm.newTodo.title).toBe('');
        expect(wrapper.vm.modal).toBe(false);
    });

    test("deletes a todo", async () => {
        const todoId = 1;
        fetchDeleteToDo.mockResolvedValueOnce({});

        await wrapper.vm.deleteTodo(todoId);

        expect(fetchDeleteToDo).toHaveBeenCalledWith(todoId, 'mocked-user-id');
    });

    test("checks date format correctly", () => {
        expect(wrapper.vm.checkDateFormat('01.01.2024')).toBe(true);
        expect(wrapper.vm.checkDateFormat('invalid date')).toBe(false);
    });

    test("formats date for display correctly", () => {
        expect(wrapper.vm.formatDateForDisplay('2024-01-01')).toBe('01.01.2024');
        expect(wrapper.vm.formatDateForDisplay('')).toBe('keine');
        expect(wrapper.vm.formatDateForDisplay(null)).toBe('keine');
    });

    test("formats date for display correctly with invalid input", () => {
        expect(wrapper.vm.formatDateForDisplay('invalid date')).toBe('ungültiges Datum');
    });

    test("edits an existing todo", async () => {
        const todoId = 1;
        const updatedTodo = { title: 'Updated Todo', description: 'Updated Description', priority: 'Hoch', deadlineDatum: '2023-10-05' };

        wrapper.vm.todos = [{ id: todoId, ...updatedTodo }];
        wrapper.vm.editTodo = { ...updatedTodo };

        await wrapper.vm.saveTodo();

        expect(wrapper.vm.todos.find(todo => todo.id === todoId).title).toBe('Updated Todo');
    });
});
