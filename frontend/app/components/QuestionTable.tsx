"use client";
import React from "react";
import { Question } from "../models/Question";
import { AnswerResponse } from "../models/AnswerResponse";
import { Answer } from "../models/Answer";
import { DIFFICULTY_LABELS } from "../models/Types";

interface QuestionTableProps {
  questions: Question[];
  answersState: AnswerResponse[];
  selectedAnswers: Answer[];
  setSelectedAnswers: (answers: Answer[]) => void;
  setAnswersState: (selectedAnswers: AnswerResponse[]) => void;
}

function onAnswerChange(
    answers: Answer[], responses: AnswerResponse[], question: Question, answer: string,
    setSelectedAnswers: (answers: Answer[]) => void, setAnswersState: (selectedAnswers: AnswerResponse[]) => void,
) {
    const otherAnswers = answers.filter(a => a.questionId !== question.id);
    const newAnswer: Answer = { questionId: question.id, answer: answer };
    const newAnswers = otherAnswers.concat(newAnswer);
    setSelectedAnswers(newAnswers);

    const newResponses = responses.filter(r => r.questionId !== question.id)
    setAnswersState(newResponses);
}

export const QuestionTable: React.FC<QuestionTableProps> = ({
  questions,
  answersState,
  selectedAnswers,
  setSelectedAnswers,
  setAnswersState,
}) => {
  return (
    <table className="w-full min-h-full border-collapse-separate">
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
                                                className="accent-teal-600 hover:cursor-pointer"
                                                onChange={() =>
                                                    onAnswerChange(
                                                        selectedAnswers, 
                                                        answersState,
                                                        question,
                                                        answer,
                                                        setSelectedAnswers,
                                                        setAnswersState
                                                    )
                                                }
                                            />
                                            <span>{answer}</span>
                                        </label>
                                    ))}
                                </div>
                            </td>
                        </tr>
                    );
                })
            )}
        </tbody>
    </table>
  );
};
