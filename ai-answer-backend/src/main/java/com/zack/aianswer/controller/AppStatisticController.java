package com.zack.aianswer.controller;

import com.zack.aianswer.common.BaseResponse;
import com.zack.aianswer.common.ResultUtils;
import com.zack.aianswer.mapper.UserAnswerMapper;
import com.zack.aianswer.model.dto.app.AppAnswerCountDTO;
import com.zack.aianswer.model.dto.app.AppAnswerResultCountDTO;
import org.apache.calcite.avatica.remote.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/statistic")
public class AppStatisticController {
    @Resource
    private UserAnswerMapper userAnswerMapper;

    @GetMapping("/appAnswerCount")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount(){
        return ResultUtils.success(userAnswerMapper.countUserByAppId());
    }

    @GetMapping("/appAnswerResultCount")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId){
        return ResultUtils.success(userAnswerMapper.answerResultCount(appId));
    }
}
