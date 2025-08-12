export interface Todo {
  text: string;
  priority: "Hoch" | "Mittel" | "Niedrig";
  description?: string;
  deadline?: string;
}
