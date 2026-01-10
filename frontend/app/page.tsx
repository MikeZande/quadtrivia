"use client"
import { useState } from "react";
import { Question } from "./models/Question";
import { Difficulty, QuestionType } from "./models/Types";
import { QuestionResponse } from "./models/QuestionResponse";

export default function Home() {
  const API_URL = "http://localhost:8080/";
  const [questions, setQuestions] = useState<Question[]>([]);;

  async function getQuestionResponse(
    amount?: number,
    category?: number,
    difficulty?: Difficulty,
    type?: QuestionType
  ) : Promise<QuestionResponse> {

    const params = new URLSearchParams();
    if (amount !== undefined) params.append("amount", amount.toString());
    if (category !== undefined) params.append("category", category.toString());
    if (difficulty) params.append("difficulty", difficulty);
    if (type) params.append("type", type);

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
        throw new Error(`Response status: ${response.status}`);
      }

      return result;
    } catch (error: any) {
      throw new Error("Something went wrong");
    }
  }

  const handleClick = async () => {
    try {
      const response: QuestionResponse = await getQuestionResponse(10);
      setQuestions(response.questions);
      console.log(response);
      console.log(questions);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-8 min-h-screen bg-slate-950">
      <div className="h-[70vh] w-full overflow-y-auto">
        <table className="bg-blue-950 w-full min-h-full border-collapse-seperate">
          <thead className="bg-blue-800 sticky top-0 z-10 border">
            <tr>
              <th className="border px-4 py-2 text-left text-l">Category</th>
              <th className="border px-4 py-2 text-left text-l">Difficulty</th>
              <th className="border px-4 py-2 text-left text-l">Question</th>
              <th className="border px-4 py-2 text-left text-l">Answers</th>
            </tr>
          </thead>
          <tbody>
            {questions.length === 0 ? (
                <tr key={1}>
                  <td colSpan={4} className="border text-center text-gray-100 text-2xl">
                    No data available
                  </td>
                </tr>
              ) : (
                questions.map((question) => (
                  <tr key={question.id} className="bg-blue-950">
                    <td className="border px-4 py-2">{question.category}</td>
                    <td className="border px-4 py-2">{question.difficulty}</td>
                    <td className="border px-4 py-2">{question.question}</td>
                    <td className="border px-4 py-2">
                      <div className="flex flex-col space-y-1">
                        {question.answers.map((answer, idx) => (
                          <label key={idx} className="flex items-center space-x-4 hover:cursor-pointer">
                            <input
                              type="radio"
                              name={question.id}
                              value={answer}
                              className="accent-sky-400"
                            />
                            <span>{answer}</span>
                          </label>
                        ))}
                      </div>
                    </td>
                  </tr>
                ))
              )}
          </tbody>
        </table>
      </div>

      <button 
        className="px-6 py-4 bg-blue-700 rounded-full hover:bg-blue-600 hover:cursor-pointer mt-8"
        onClick={handleClick}
      >
        Generate New Questions
      </button>
    </div>
  );
}
