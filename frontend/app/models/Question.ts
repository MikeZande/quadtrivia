import { Difficulty, QuestionType } from "./Types";

export interface Question {
    id: string;
    type: QuestionType;
    difficulty: Difficulty;
    category: string;
    question: string;
    answers: string[];
}