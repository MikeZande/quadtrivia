import { Question } from "./Question";
import { ResponseCode } from "./Types";

export interface QuestionResponse {
  responseCode: ResponseCode;
  questions: Question[];
}