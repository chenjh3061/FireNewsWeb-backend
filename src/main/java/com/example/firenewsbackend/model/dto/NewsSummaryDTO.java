package com.example.firenewsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsSummaryDTO {
    private String summary;

    public NewsSummaryDTO(String summary) {
        this.summary = summary;
    }

}
