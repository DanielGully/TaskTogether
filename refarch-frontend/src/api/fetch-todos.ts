import {defaultResponseHandler, deleteConfig, getConfig, postConfig, putConfig} from "@/api/fetch-utils.ts";

export function fetchToDosByPriority(priority, userId) {
    return fetch(`api/backend-service/todos/priority?priority=${priority}&userId=${userId}`, getConfig())
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}

export function fetchCreateToDo(toDoRequestDTO, userId) {
    return fetch(`api/backend-service/todos?userId=${userId}`, postConfig(toDoRequestDTO))
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}

export function fetchUpdateToDo(todoId, toDoRequestDTO, userId) {
    return fetch(`api/backend-service/todos/${todoId}?userId=${userId}`, putConfig(toDoRequestDTO))
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}

export function fetchDeleteToDo(todoId, userId) {
    return fetch(`api/backend-service/todos/${todoId}?userId=${userId}`, deleteConfig())
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

export function fetchToDosByDeadline(userId) {
    return fetch(`api/backend-service/todos/sorted?userId=${userId}`, getConfig())
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}