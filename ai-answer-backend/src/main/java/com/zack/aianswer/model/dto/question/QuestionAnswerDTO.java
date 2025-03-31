package com.zack.aianswer.model.dto.question;

import lombok.Data;

import java.io.Serializable;

<<<<<<< HEAD
/**
 * @description:用户答案封装类/给ai用
 **/
@Data
public class QuestionAnswerDTO implements Serializable {
    private static final long serialVersionUID = -7094964824056449128L;
    private String title;
    private String answer;
=======
@Data
public class UserMessageRequest implements Serializable {
    private static final long serialVersionUID = -7094964824056449128L;
    private String appName;
    private String appDesc;
    private String appType;
    private Integer questionNum;
    private Integer optionNum;


>>>>>>> 2b2c7a18520a61c296dce0034fb88bba0b093360
}
