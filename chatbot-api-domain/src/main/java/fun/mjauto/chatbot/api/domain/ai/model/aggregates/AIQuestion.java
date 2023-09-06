package fun.mjauto.chatbot.api.domain.ai.model.aggregates;

import fun.mjauto.chatbot.api.domain.ai.model.vo.Message;
import org.springframework.beans.factory.annotation.Value;

public class AIQuestion {
    private String url;
    private String model;

    private Message message;

    private double temperature;

    public AIQuestion(String url, String model, Message message, double temperature) {
        this.url = url;
        this.model = model;
        this.message = message;
        this.temperature = temperature;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
