package com.example.firenewsbackend.model.dto;

public class NewsSummaryDTO {
    private String summary;

    public NewsSummaryDTO(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
