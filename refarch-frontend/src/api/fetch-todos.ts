import {defaultResponseHandler, getConfig, postConfig, putConfig} from "@/api/fetch-utils.ts";

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
