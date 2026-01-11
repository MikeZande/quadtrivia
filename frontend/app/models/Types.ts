// Arrays iterated over by select boxes.
export const DIFFICULTIES = ["easy", "medium", "hard"] as const;
export const QUESTION_TYPES = ["multiple", "boolean"] as const;
export const CATEGORIES = ["10", "11"] as const; // TODO: get the proper categories somehow.

export type Category = typeof CATEGORIES[number];
export type Difficulty = typeof DIFFICULTIES[number];
export type QuestionType = typeof QUESTION_TYPES[number];
export type ResponseCode = "SUCCESS" | "NO_RESULTS" | "INVALID_PARAMETER" | "RATE_LIMIT_EXCEEDED";