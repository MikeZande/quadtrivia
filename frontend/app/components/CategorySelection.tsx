"use client";

import React from "react";
import { Category } from "../models/Category";

interface CategorySelectionProps {
  categories: Category[];
  value: number | undefined;
  onChange: (val: number | undefined) => void;
}

export const CategorySelection: React.FC<CategorySelectionProps> = ({ 
    categories, 
    value, 
    onChange 
}) => {
  return (
    <div className="flex gap-4">
      <span className="w-40">Category:</span>
      <select
        className="bg-slate-900 border-3 border-gray-300 rounded-md w-60 hover:cursor-pointer"
        value={value ?? ""}
        onChange={(e) => onChange(e.target.value ? Number(e.target.value) : undefined)}
      >
        <option value="">All</option>
        {categories.map((category) => (
          <option key={category.id} value={category.id}>
            {category.name}
          </option>
        ))}
      </select>
    </div>
  );
};