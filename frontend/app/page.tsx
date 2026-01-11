"use client"
import { useState, useEffect } from "react";
import { Difficulty, QuestionType, ResponseCode } from "./models/Types";
import { Question } from "./models/Question";
import { QuestionResponse } from "./models/QuestionResponse";
import { Category } from "./models/Category";
import { getCategories, getQuestions, submitAnswers } from "./services/api";
import { Answer } from "./models/Answer";
import { AnswerResponse } from "./models/AnswerResponse";
import { QuestionTable } from "./components/QuestionTable";
import { CategorySelection } from "./components/CategorySelection";
import { QuestionTypeSelection } from "./components/QuestionTypeSelection";
import { DifficultySelection } from "./components/DifficultySelection";

export default function Home() {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [responseCode, setResponseCode] = useState<ResponseCode | null>(null);
  const [selectedAnswers, setSelectedAnswers] = useState<Answer[]>([]);
  const [answersState, setAnswersState] = useState<AnswerResponse[]>([]);

  // Query params for getQuestions.
  const [questionAmount, setQuestionAmount] = useState<number>(5);
  const [category, setCategory] = useState<number | undefined>(undefined);
  const [difficulty, setDifficulty] = useState<Difficulty | undefined>(undefined);
  const [questionType, setQuestionType] = useState<QuestionType | undefined>(undefined);

  // Fetch possible categories at page load.
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const categories = await getCategories();
        setCategories(categories);
      } catch (error) {
        console.error(error);
      }
    };

    fetchCategories();
  }, []);

  const loadQuestions = async () => {
    try {
      const response: QuestionResponse = await getQuestions(
        questionAmount,
        category,
        difficulty,
        questionType
      );
      setQuestions(response.questions);
      setResponseCode(response.responseCode);
      // Clear answers array:
      setSelectedAnswers([]);
    } catch (error) {
      console.error(error);
    }
  };

  const checkAnswers = async () => {
    try {
      const response: AnswerResponse[] = await submitAnswers(selectedAnswers);
      setAnswersState(response);
    } catch (error) {
      console.error(error);
    }
  }

  function handleNrInput(value: number) {
    if (value < 1) value = 1;
    if (value > 50) value = 50;
    setQuestionAmount(value);
  }

  return (
    <div className="p-8 min-h-screen bg-slate-950">
      <div className="h-[65vh] w-full overflow-y-auto mb-8">
        <QuestionTable 
          questions={questions}
          answersState={answersState}
          selectedAnswers={selectedAnswers}
          setSelectedAnswers={setSelectedAnswers}
          setAnswersState={setAnswersState}
        />
      </div>

      <div className="flex justify-between items-start">
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
              onChange={(e) => setQuestionAmount(Number(e.target.value))}
              onBlur={() => handleNrInput(questionAmount)}
            />
          </div>

          {/* Category */}
          <CategorySelection
            categories={categories}
            value={category}
            onChange={setCategory}
          />

          {/* Difficulty */}
          <DifficultySelection
            onChange={setDifficulty}
          />

          {/* Question Type */}
          <QuestionTypeSelection
            onChange={setQuestionType}
          />

          {/* Generate questions button */}
          <button 
            className="px-6 py-4 bg-teal-600 rounded-full hover:bg-teal-500 hover:cursor-pointer mt-2"
            onClick={loadQuestions}
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

        {/* Check Answers button */}
        <button 
          className="px-6 py-4 bg-teal-600 rounded-full hover:bg-teal-500 hover:cursor-pointer"
          onClick={checkAnswers}
        >
          Check Answers
        </button>
      </div>
    </div>
  );
}
