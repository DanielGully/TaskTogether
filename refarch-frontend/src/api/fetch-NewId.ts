import {defaultResponseHandler, getConfig} from "@/api/fetch-utils.ts";

/**
 * Requests a new ID from the server.
 */
export function requestNewId(): Promise<number> {
    return fetch("api/backend-service/todos/generate-id", getConfig())
        .then((response) => {
            defaultResponseHandler(response);
            return response.json();
        })
        .catch((err) => {
            defaultResponseHandler(err);
        });
}