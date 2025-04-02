package com.zack.aianswer.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zack.aianswer.model.dto.app.AppAnswerCountDTO;
import com.zack.aianswer.model.dto.app.AppAnswerResultCountDTO;
import com.zack.aianswer.model.entity.UserAnswer;

import java.util.List;

/**
* @author mczq
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2025-03-05 09:55:51
* @Entity generator.domain.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {
//   统计每个应用的答题者数量
    List<AppAnswerCountDTO> countUserByAppId();
    List<AppAnswerResultCountDTO> answerResultCount(Long appId);
}




