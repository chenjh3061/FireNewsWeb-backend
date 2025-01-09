package com.example.firenewsbackend.model.vo;

import lombok.Data;

@Data
public class DashboardVO {

    private Long userNum;

    private Long articleNum;

    private Long todayViewCount;

    private Long runningDays;

}
