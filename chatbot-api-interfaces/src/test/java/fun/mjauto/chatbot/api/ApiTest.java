package fun.mjauto.chatbot.api;

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
import org.junit.Test;

import java.io.IOException;

public class ApiTest {
    @Test
    public void test_chatGPT() throws IOException {

        // 创建一个HttpHost对象，用于指定代理服务器的主机名和端口号
        HttpHost proxy = new HttpHost("127.0.0.1", 10809);

        // 创建RequestConfig对象并设置代理
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        // 使用RequestConfig来创建CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();

        // 创建一个HTTP POST请求，指定目标URL
        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");

        // 设置HTTP请求头，包括Content-Type和Authorization
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer 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");

        // 构建请求参数的JSON字符串
        String paramJson = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"请介绍一下大语言模型。\"}], \"temperature\": 0.7}\n";

        // 创建一个StringEntity，用于将JSON参数添加到请求体中
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        // 执行HTTP POST请求
        CloseableHttpResponse response = httpClient.execute(post);

        // 检查HTTP响应的状态码
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 如果状态码为200（OK），则获取响应内容并打印
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            // 如果状态码不是200，则打印状态码
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }
}
