// Arrays iterated over by select boxes.
export const DIFFICULTIES = ["easy", "medium", "hard"] as const;
export const QUESTION_TYPES = ["multiple", "boolean"] as const;

// Neat labels for select boxes.
export const DIFFICULTY_LABELS: Record<Difficulty, string> = {
  "easy": "Easy",
  "medium": "Medium",
  "hard": "Hard",
};
export const QUESTION_TYPE_LABELS: Record<QuestionType, string> = {
  "multiple": "Multiple Choice",
  "boolean": "True or False",
};

export type Difficulty = typeof DIFFICULTIES[number];
export type QuestionType = typeof QUESTION_TYPES[number];
export type ResponseCode = "SUCCESS" | "NO_RESULTS" | "INVALID_PARAMETER" | "RATE_LIMIT_EXCEEDED";