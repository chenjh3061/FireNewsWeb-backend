package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.manager.AiManager;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.model.dto.NewsSummaryDTO;
import com.example.firenewsbackend.model.dto.UserQuestionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiManager aiManager;

    /**
     * 获取新闻总结
     *
     * @param params 新闻内容
     * @return AI 生成的总结
     */
    @PostMapping("/generateSummary")
    public BaseResponse<NewsSummaryDTO> generateNewsSummary(@RequestBody Map<String, String> params) {
//        System.out.println("新闻内容"+newsContent);
        StpUtil.checkLogin();
        String newsContent = params.get("content");
        String summary = aiManager.generateNewsSummary(newsContent);
        NewsSummaryDTO response = new NewsSummaryDTO(summary);
        return ResultUtils.success(response);
    }

    /**
     * 处理用户提问
     *
     * @param userQuestion 用户问题
     * @return AI 的回答
     */
    @PostMapping("/askQuestion")
    public BaseResponse<UserQuestionResponseDTO> askQuestion(@RequestBody String userQuestion) {
        StpUtil.checkLogin();
        String answer = aiManager.processUserQuestion(userQuestion);
        UserQuestionResponseDTO response = new UserQuestionResponseDTO(answer);
        return ResultUtils.success(response);
    }
}
