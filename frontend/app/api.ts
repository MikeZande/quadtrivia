import { Answer } from "./models/Answer";
import { AnswerResponse } from "./models/AnswerResponse";
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
        console.error("Something went wrong: ", error);
        throw new Error("Failed to fetch questions");
    }
}

// Fetch categories
export async function getCategories(): Promise<Category[]> {
    try {
        const response = await fetch("https://opentdb.com/api_category.php");
        const categories = await response.json();
        return categories.trivia_categories;
    } catch (error) {
        console.error("Something went wrong: ", error);
        throw new Error("Failed to fetch categories");
    }
}

// Asks the API to check the answers.
export async function submitAnswers(answers: Answer[]): Promise<AnswerResponse[]> {
    const uri = API_URL + "checkanswers"
    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(answers)
    };

    try {
        const response = await fetch(uri, requestOptions);
        const result: AnswerResponse[] = await response.json();

        if (!response.ok) {
            throw new Error("Response status: " + response.status);
        }

        return result;
    } catch (error) {
        console.error("Something went wrong: ", error);
        throw new Error("Failed to check answers");
    }
}