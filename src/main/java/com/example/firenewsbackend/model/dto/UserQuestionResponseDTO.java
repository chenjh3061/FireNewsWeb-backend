package com.example.firenewsbackend.model.dto;

public class UserQuestionResponseDTO {
    private String answer;

    public UserQuestionResponseDTO(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
