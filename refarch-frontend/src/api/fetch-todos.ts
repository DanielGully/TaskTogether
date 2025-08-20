import {defaultResponseHandler, deleteConfig, getConfig, postConfig, putConfig} from "@/api/fetch-utils.ts";

export function fetchToDosByPriority(priority) {
    return fetch(`api/backend-service/todos/priority?priority=${priority}`, getConfig())
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}

export function fetchCreateToDo(toDoRequestDTO) {
    return fetch(`api/backend-service/todos`, postConfig(toDoRequestDTO))
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}

export function fetchUpdateToDo(todoId, toDoRequestDTO) {
    return fetch(`api/backend-service/todos/${todoId}`, putConfig(toDoRequestDTO))
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}

export function fetchDeleteToDo(todoId) {
    return fetch(`api/backend-service/todos/${todoId}`, deleteConfig())
        .then((response) => {
            if (!response.ok) {
                throw new Error("Fehler beim Löschen des ToDos: " + response.statusText);
            }
            return response.text().then(text => {
                if (text) {
                    return JSON.parse(text);
                }
                return null;
            });
        })
        .catch((err) => {
            console.debug("Fehler bei fetchDeleteToDo:", err);
            throw err;
        });
}

export function fetchToDosByDeadline() {
    return fetch(`api/backend-service/todos/deadline`, getConfig())
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}