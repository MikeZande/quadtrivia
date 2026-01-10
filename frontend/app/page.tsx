"use client"
import { useEffect, useState } from "react";

export default function Home() {
  const API_URL = "http://localhost:8080/";
  const [questions, setQuestions] = useState(null);

  async function getQuestions(
    amount?: number,
    category?: number,
    difficulty?: string,
    type?: string
  ) {
    const params = new URLSearchParams();
    if (amount) params.append("amount", amount.toString());
    if (category) params.append("category", category.toString());
    if (difficulty) params.append("difficulty", difficulty);
    if (type) params.append("type", type);

    const uri = API_URL + "questions?" + params;
    console.log(uri);

    try {
      const response = await fetch(uri);
      const result = await response.json();

      if (response.status === 429) {
        console.log(result);
        console.warn("Rate limit exceeded. Please wait 5 seconds before requesting.");
        return result;
      }

      if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
      }

      console.log(result);
      return result;
    } catch (error: any) {
      console.error(error.message);
    }
  }

  return (
    <div className="p-8 min-h-screen bg-slate-950">
      <button 
        className="px-4 py-2 bg-blue-500 rounded hover:bg-blue-600"
        onClick={() => getQuestions()}
      >
        Request Questions
      </button>
    </div>
  );
}
