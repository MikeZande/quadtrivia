import { Category } from "./models/Category";
import { QuestionResponse } from "./models/QuestionResponse";
import { Difficulty, QuestionType } from "./models/Types";

const API_URL = "http://localhost:8080/";

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

    try {
        const response = await fetch(uri);
        const result: QuestionResponse = await response.json();

        // Rate limit exceeded.
        if (response.status === 429) {
            console.warn("Rate limit exceeded. Please wait 5 seconds before requesting.");
            return result;
        }

        if (!response.ok) {
            throw new Error("Response status: " + response.status);
        }

        return result;
    } catch (error) {
        throw new Error("Failed to fetch questions");
    }
}

// Fetch categories
export async function getCategories(): Promise<Category[]> {
    try {
        const res = await fetch("https://opentdb.com/api_category.php");
        const categories = await res.json();
        return categories.trivia_questions;
    } catch (err) {
        throw new Error("Failed to fetch categories");
    }
}

