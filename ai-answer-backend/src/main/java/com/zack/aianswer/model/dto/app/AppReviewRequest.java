package com.zack.aianswer.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppReviewRequest implements Serializable {

    private static final long serialVersionUID = 5913982265231078868L;

    private Long appid;

    private Integer reviewStatus;

    private String message;


}
