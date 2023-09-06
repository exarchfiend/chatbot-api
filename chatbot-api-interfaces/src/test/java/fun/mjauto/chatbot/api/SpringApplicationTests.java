package fun.mjauto.chatbot.api;

import fun.mjauto.chatbot.api.domain.ai.IOpenAI;
import fun.mjauto.chatbot.api.domain.ai.model.aggregates.AIQuestion;
import fun.mjauto.chatbot.api.domain.ai.model.vo.Message;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(SpringApplicationTests.class);

    @Value("${chatBot-api.openAiKey}")
    String openAiKey;
    @Resource
    private IOpenAI openAI;

    @Test
    public void test_openAi() throws Exception {
        String response = openAI.doChatGPT(new AIQuestion("https://api.openai.com/v1/chat/completions",
                "gpt-3.5-turbo",
                new Message("user",
                        "请介绍一下大语言模型。"),
                0.7), this.openAiKey);
        logger.info("测试结果：{}", response);
    }
}