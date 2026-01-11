"use client";

import React from "react";
import { DIFFICULTIES, Difficulty, DIFFICULTY_LABELS } from "../models/Types";

interface DifficultySelectionProps {
  onChange: (val: Difficulty | undefined) => void;
}

export const DifficultySelection: React.FC<DifficultySelectionProps> = ({
    onChange 
}) => {
  return (
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
                onChange(
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
  );
};