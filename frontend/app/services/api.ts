import { Answer } from "../models/Answer";
import { AnswerResponse } from "../models/AnswerResponse";
import { Category } from "../models/Category";
import { QuestionResponse } from "../models/QuestionResponse";
import { Difficulty, QuestionType } from "../models/Types";

const API_URL = "http://localhost:8080/";
const includeCredentials: RequestCredentials = "include";

// Fetch questions
export async function getQuestions(
    amount: number,
    category?: number,
    difficulty?: Difficulty,
    type?: QuestionType
): Promise<QuestionResponse> {

    const params = new URLSearchParams();
    params.append("amount", amount.toString());
    if (category !== undefined) params.append("category", category.toString());
    if (difficulty !== undefined) params.append("difficulty", difficulty);
    if (type !== undefined) params.append("type", type);

    const uri = API_URL + "questions?" + params;

    const response = await fetch(uri, {credentials: includeCredentials});
    const result: QuestionResponse = await response.json();

    // Rate limit exceeded.
    if (response.status === 429) {
        return result;
    }

    if (!response.ok) {
        throw new Error("Something went wrong while loading questions: " + response.status);
    }

    return result;
}

// Fetch categories
export async function getCategories(): Promise<Category[]> {
    const response = await fetch("https://opentdb.com/api_category.php");
    const categories = await response.json();

    if (!response.ok) {
        throw new Error("Something went wrong while loading categories: " + response.status);
    }

    return categories.trivia_categories;
}

// Asks the API to check the answers.
export async function submitAnswers(answers: Answer[]): Promise<AnswerResponse[]> {
    const uri = API_URL + "checkanswers"
    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(answers),
        credentials: includeCredentials,
    };

    const response = await fetch(uri, requestOptions);
    const result: AnswerResponse[] = await response.json();

    if (!response.ok) {
        throw new Error("Something went wrong while checking answers: " + response.status);
    }

    return result;
}