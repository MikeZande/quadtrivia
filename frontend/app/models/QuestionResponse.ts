import { Question } from "./Question";
import { ResponseCode } from "./Types";

export interface QuestionResponse {
  results: Question[];
  responseCode: ResponseCode;
}