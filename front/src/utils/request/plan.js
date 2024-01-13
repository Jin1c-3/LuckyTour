import { as } from "vitest/dist/reporters-5f784f42";
import service from "../request";

async function chat(data) {
  const response = await service.post("/chat", data);
  const result = response.data;
  return result;
}
