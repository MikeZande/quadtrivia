"use client"
import { useState } from "react";
import { Question } from "./models/Question";
import { Difficulty, QuestionType, ResponseCode, Category, DIFFICULTIES, QUESTION_TYPES, CATEGORIES } from "./models/Types";
import { QuestionResponse } from "./models/QuestionResponse";

export default function Home() {
  const API_URL = "http://localhost:8080/";
  const [questions, setQuestions] = useState<Question[]>([]);;
  const [responseCode, setResponseCode] = useState<ResponseCode | null>(null);

  // Query params for getQuestions.
  const [questionAmount, setQuestionAmount] = useState<number>(5);
  const [category, setCategory] = useState<Category | undefined>(undefined);
  const [difficulty, setDifficulty] = useState<Difficulty | undefined>(undefined);
  const [questionType, setQuestionType] = useState<QuestionType | undefined>(undefined);

  async function getQuestionResponse() : Promise<QuestionResponse> {

    const params = new URLSearchParams();
    params.append("amount", questionAmount.toString());
    if (category !== undefined) params.append("category", category);
    if (difficulty !== undefined) params.append("difficulty", difficulty);
    if (questionType !== undefined) params.append("type", questionType);

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
    } catch (error) {
      throw new Error("Something went wrong");
    }
  }

  const handleClick = async () => {
    try {
      const response: QuestionResponse = await getQuestionResponse();
      setQuestions(response.questions);
      setResponseCode(response.responseCode);
      console.log(response);
      console.log(questions);
    } catch (err) {
      console.error(err);
    }
  };

  function handleNrInput(e: React.ChangeEvent<HTMLInputElement>) {
    let value = Number(e.target.value);
    if (value < 1) value = 1;
    if (value > 50) value = 50;
    setQuestionAmount(value);
  }

  return (
    <div className="p-8 min-h-screen bg-slate-950">
      <div className="h-[65vh] w-full overflow-y-auto mb-8">
        <table className="w-full min-h-full border-collapse-seperate">
          <thead className="bg-teal-600 sticky top-0 z-10 border">
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
                  <tr key={question.id} className="bg-slate-900">
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
                              className="accent-teal-600 hover:cursor-pointer"
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

      <div className="flex flex-col w-fit gap-2">
        {/* Amount of questions */}
        <div className="flex gap-4">
          <span className="w-40">
            Number of Questions:
          </span>

          <input
            className="
              bg-slate-900 
              border-3 border-gray-300
              rounded-md
              w-60
              hover:cursor-pointer"
            type="number"
            min={1}
            max={50}
            value={questionAmount}
            onChange={(e) => handleNrInput(e)}
          />
        </div>

        {/* Category */}
        <div className="flex gap-4">
          <span className="w-40">
            Category:
          </span>

          <select 
            className="
              bg-slate-900 
              border-3 border-gray-300
              rounded-md
              w-60
              hover:cursor-pointer"
            onChange={(e) => 
              setCategory(
                e.target.value === "" ? undefined : (e.target.value as Category)
              )
            }
          >
            <option value=""> All </option>
            {CATEGORIES.map((value) => (
              <option key={value} value={value}>
                {value}
              </option>
            ))}
          </select>
        </div>

        {/* Difficulty */}
        <div className="flex gap-4">
          <span className="w-40">
            Difficulty:
          </span>

          <select 
            className="
              bg-slate-900 
              border-3 border-gray-300
              rounded-md
              w-60
              hover:cursor-pointer"
            onChange={(e) => 
              setDifficulty(
                e.target.value === "" ? undefined : (e.target.value as Difficulty)
              )
            }
          >
            <option value=""> All </option>
            {DIFFICULTIES.map((value) => (
              <option key={value} value={value}>
                {value}
              </option>
            ))}
          </select>
        </div>

        {/* Question Type */}
        <div className="flex gap-4">
          <span className="w-40">
            Question Type:
          </span>

          <select 
            className="
              bg-slate-900 
              border-3 border-gray-300
              rounded-md
              w-60
              hover:cursor-pointer"
            onChange={(e) => 
              setQuestionType(
                e.target.value === "" ? undefined : (e.target.value as QuestionType)
              )
            }
          >
            <option value=""> All </option>
            {QUESTION_TYPES.map((value) => (
              <option key={value} value={value}>
                {value}
              </option>
            ))}
          </select>
        </div>

        {/* Generate questions button */}
        <button 
          className="px-6 py-4 bg-teal-600 rounded-full hover:bg-teal-500 hover:cursor-pointer mt-2"
          onClick={handleClick}
        >
          Generate New Questions
        </button>

        {responseCode && (
          <p 
            className={`mt-3 text-m text-center text-wrap ${
              responseCode === "SUCCESS" 
                ? "text-green-500"
                : "text-red-500"
            }`}
          >
            {responseCode}
          </p>
        )}
      </div>
    </div>
  );
}
