package com.zack.aianswer.model.dto.question;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionContentDTO {
    private List<Option> options;
    private String title;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        private String result;
        private String value;
        private String key;

    }
}


