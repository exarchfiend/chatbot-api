package fun.mjauto.chatbot.api.domain.ai.model.aggregates;

import fun.mjauto.chatbot.api.domain.ai.model.vo.Choices;
import fun.mjauto.chatbot.api.domain.ai.model.vo.Usage;

import java.util.List;

public class AIAnswer {
    private String id;

    private String object;

    private int created;

    private String model;

    private List<Choices> choices;

    private Usage usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Choices> getChoices() {
        return choices;
    }

    public void setChoices(List<Choices> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}