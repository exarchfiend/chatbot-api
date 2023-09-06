package fun.mjauto.chatbot.api.domain.ai;

import fun.mjauto.chatbot.api.domain.ai.model.aggregates.AIQuestion;

public interface IOpenAI {
    String doChatGPT(AIQuestion question, String openAiKey) throws Exception;
}
