package com.zack.aianswer.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserMessageRequest implements Serializable {
    private static final long serialVersionUID = -7094964824056449128L;
    private String appName;
    private String appDesc;
    private String appType;
    private Integer questionNum;
    private Integer optionNum;


}
