"use client"
import { useEffect, useState } from "react";

export default function Home() {
  const API_URL = "http://localhost:8080/";
  const [welcomeMsg, setWelcomeMsg] = useState("Default value");

  useEffect(() => {
    const fetchMessage = async () => {
      try {
        const res = await fetch(API_URL);
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        const data = await res.text();
        setWelcomeMsg(data);
      } catch (error) {
        console.error("Failed to fetch welcome message:", error);
      }
    };

    fetchMessage();
  }, []);

  return (
    <div>
      Hey + {welcomeMsg} 
    </div>
  );
}
