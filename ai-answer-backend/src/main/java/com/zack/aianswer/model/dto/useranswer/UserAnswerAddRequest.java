package com.zack.aianswer.model.dto.useranswer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建用户答题记录请求
 *
 * @author <a href="https://github.com/iKeepRun">zack</a>
 */
@Data
public class UserAnswerAddRequest implements Serializable {

    private Long id;
    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

    private static final long serialVersionUID = 1L;
}