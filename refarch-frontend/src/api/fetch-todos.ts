import {defaultResponseHandler, getConfig} from "@/api/fetch-utils.ts";

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
