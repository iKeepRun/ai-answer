package com.zack.aianswer;

import com.zack.aianswer.manager.AiManager;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AiTest {
    @Resource
    AiManager aiManager;
    @Test
    public void test1(){
//        String answer = aiManager.getAnswer("你是谁");
//        System.out.println("model output:" + answer);

    }
}
