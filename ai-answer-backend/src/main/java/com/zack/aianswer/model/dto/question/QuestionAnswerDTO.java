package com.zack.aianswer.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:用户答案封装类/给ai用
 **/
@Data
public class QuestionAnswerDTO implements Serializable {
    private static final long serialVersionUID = -7094964824056449128L;
    private String title;
    private String answer;
}
