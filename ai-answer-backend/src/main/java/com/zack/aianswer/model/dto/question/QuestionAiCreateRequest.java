package com.zack.aianswer.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class QuestionAiCreateRequest {
//    private String appId;
    private String appName;
    private String appDesc;
    private String appType;
    private Integer questionNum;
    private Integer optionNum;
}
