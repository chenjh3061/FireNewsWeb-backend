package com.example.firenewsbackend.manager;

import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用 AI 调用能力
 */
@Component
public class AiManager {
    @Resource
    private ClientV4 clientV4;

    // 稳定的随机数
    private static final float STABLE_TEMPERATURE = 0.05f;

    /**
     * 同步请求（生成新闻总结）
     *
     * @param newsContent 新闻内容
     * @return AI 生成的总结
     */
    public String generateNewsSummary(String newsContent) {
        String systemMessage = "你是一个新闻总结专家。请根据以下新闻内容生成一个简洁的总结，保持重点突出，清晰明了。";
        return doSyncRequest(systemMessage, newsContent, STABLE_TEMPERATURE);
    }

    /**
     * 同步请求（处理用户提问）
     *
     * @param userQuestion 用户提问
     * @return AI 回答
     */
    public String processUserQuestion(String userQuestion) {
        String systemMessage = "你是一个智能问答助手，请回答用户的提问。";
        return doSyncRequest(systemMessage, userQuestion, STABLE_TEMPERATURE);
    }

    /**
     * 通用请求
     *
     * @param systemMessage 系统消息
     * @param userMessage 用户消息
     * @param temperature 温度控制
     * @return AI 返回的回答或总结
     */
    public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        if (systemMessage == null || userMessage == null || temperature == null) {
            throw new IllegalArgumentException("Invalid parameters: systemMessage, userMessage, or temperature is null");
        }
        List<ChatMessage> chatMessageList = new ArrayList<>();
        chatMessageList.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage));
        chatMessageList.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));
        return doRequest(chatMessageList, temperature);
    }

    /**
     * 发送请求
     *
     * @param messages 消息列表
     * @param temperature 温度控制
     * @return AI 返回的响应
     */
    public String doRequest(List<ChatMessage> messages, Float temperature) {
        // 构建请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .temperature(temperature)
                .messages(messages)
                .build();
        try {
            ModelApiResponse response = clientV4.invokeModelApi(chatCompletionRequest);
            return response.getData().getChoices().get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
