package fun.mjauto.chatbot.api.domain.ai.service;

import com.alibaba.fastjson.JSON;
import fun.mjauto.chatbot.api.domain.ai.IOpenAI;
import fun.mjauto.chatbot.api.domain.ai.model.aggregates.AIAnswer;
import fun.mjauto.chatbot.api.domain.ai.model.aggregates.AIQuestion;
import fun.mjauto.chatbot.api.domain.ai.model.vo.Choices;
import fun.mjauto.chatbot.api.domain.wx.controller.WeChatController;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAI implements IOpenAI {
    private final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Value("${chatBot-api.hostname}")
    private String hostname;
    @Value("${chatBot-api.port}")
    private Integer port;

    @Override
    public String doChatGPT(AIQuestion question, String openAiKey) throws Exception {
        // 创建一个HttpHost对象，用于指定代理服务器的主机名和端口号
        HttpHost proxy = new HttpHost(this.hostname, this.port);

        // 创建RequestConfig对象并设置代理
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        // 使用RequestConfig来创建CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();

        // 创建一个HTTP POST请求，指定目标URL
        HttpPost post = new HttpPost(question.getUrl());

        // 设置HTTP请求头，包括Content-Type和Authorization
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + openAiKey);

        // 构建请求参数的JSON字符串
        String paramJson = "{\"model\": \"" + question.getModel() + "\"," +
                " \"messages\": [{\"role\": \"" + question.getMessage().getRole() + "\"," +
                " \"content\": \"" + question.getMessage().getContent() + "\"}]," +
                " \"temperature\": " + question.getTemperature() + "}\n";

        // 创建一个StringEntity，用于将JSON参数添加到请求体中
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        // 执行HTTP POST请求
        CloseableHttpResponse response = httpClient.execute(post);

        // 检查HTTP响应的状态码
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 如果状态码为200（OK），则获取响应内容并打印

            // 从HTTP响应实体中获取JSON字符串
            String jsonStr = EntityUtils.toString(response.getEntity());
            // 使用JSON解析库将JSON字符串解析为AIAnswer对象
            AIAnswer aiAnswer = JSON.parseObject(jsonStr, AIAnswer.class);
            // 创建一个StringBuilder对象来构建答案
            StringBuilder answers = new StringBuilder();
            // 获取AIAnswer对象中的choices列表
            List<Choices> choices = aiAnswer.getChoices();
            // 遍历choices列表并将每个choice的内容追加到answers中
            for (Choices choice : choices) {
                answers.append(choice.getMessage().getContent());
            }
            // 返回构建的答案字符串
            logger.info("AI的回答：" + answers);
            return answers.toString();
        } else {
            // 如果状态码不是200，则打印状态码并抛出异常

            // 抛出运行时异常，包含错误信息和HTTP响应的状态码
            logger.error("访问OpenAI发生错误：" + response.getStatusLine().getStatusCode(), new RuntimeException());
            return "AI抛锚了说是";
        }
    }
}
