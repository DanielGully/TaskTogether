export type Priority = "Hoch" | "Mittel" | "Niedrig";

export interface ToDoResponseDTO {
  id: string;
  title: string;
  description: string;
  priority: Priority;
  deadlineDatum: string;
}
