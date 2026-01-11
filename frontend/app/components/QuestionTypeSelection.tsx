"use client";

import React from "react";
import { QUESTION_TYPE_LABELS, QUESTION_TYPES, QuestionType } from "../models/Types";

interface QuestionTypeSelectionProps {
  onChange: (val: QuestionType | undefined) => void;
}

export const QuestionTypeSelection: React.FC<QuestionTypeSelectionProps> = ({
    onChange 
}) => {
  return (
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
                onChange(
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
  );
};