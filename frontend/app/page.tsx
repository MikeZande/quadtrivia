"use client"
import { useState, useEffect } from "react";
import { Difficulty, QuestionType, ResponseCode, DIFFICULTIES, QUESTION_TYPES, DIFFICULTY_LABELS, QUESTION_TYPE_LABELS } from "./models/Types";
import { Question } from "./models/Question";
import { QuestionResponse } from "./models/QuestionResponse";
import { Category } from "./models/Category";
import { getCategories, getQuestions, submitAnswers } from "./api";
import { Answer } from "./models/Answer";
import { AnswerResponse } from "./models/AnswerResponse";

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
                questions.map((question) => {
                  const answerState = answersState.find(a => a.questionId === question.id);

                  let rowClass = "bg-slate-950";
                  if (answerState) {
                    rowClass = answerState.isCorrectAnswer ? "bg-green-950" : "bg-red-950";
                  }
                
                return (
                  <tr key={question.id} className={rowClass}>
                    <td className="border px-4 py-2">{question.category}</td>
                    <td className="border px-4 py-2">{DIFFICULTY_LABELS[question.difficulty]}</td>
                    <td className="border px-4 py-2">{question.question}</td>
                    <td className="border px-4 py-2">
                      <div className="flex flex-col space-y-1">
                        {question.answers.map((answer, idx) => (
                          <label key={idx} className="flex items-center space-x-4 hover:cursor-pointer">
                            <input
                              type="radio"
                              name={question.id}
                              value={answer}
                              onChange={() => {
                                setSelectedAnswers(prev => {
                                  const otherAnswers = prev.filter(answer => answer.questionId !== question.id); // Remove old answer.
                                  const newAnswer: Answer = { questionId: question.id, answer: answer }; // Add new answer.
                                  return otherAnswers.concat(newAnswer);
                                });

                                // Remove the answer state of this question as it has been changed.
                                setAnswersState(prev => prev.filter(a => a.questionId !== question.id));
                              }}
                              className="accent-teal-600 hover:cursor-pointer"
                            />
                            <span>{answer}</span>
                          </label>
                        ))}
                      </div>
                    </td>
                  </tr>
                );})
              )}
          </tbody>
        </table>
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
                  e.target.value ? (Number(e.target.value)) : undefined
                )
              }
            >
              <option value=""> All </option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
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
                  {DIFFICULTY_LABELS[value]}
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
                  {QUESTION_TYPE_LABELS[value]}
                </option>
              ))}
            </select>
          </div>

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
