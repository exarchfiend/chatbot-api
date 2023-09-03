package fun.mjauto.chatbot.api.domain.ai;

public interface IOpenAI {
    String doChatGPT(String question) throws Exception;
}
