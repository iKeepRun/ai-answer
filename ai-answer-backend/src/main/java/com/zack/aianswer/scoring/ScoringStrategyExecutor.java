package com.zack.aianswer.scoring;

import com.zack.aianswer.common.ErrorCode;
import com.zack.aianswer.exception.BusinessException;
import com.zack.aianswer.model.entity.App;
import com.zack.aianswer.model.entity.UserAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class ScoringStrategyExecutor {
    @Resource
    List<ScoringStrategy> scoringStrategies;
     public UserAnswer doScore(App app,List<String> choices) {
         for (ScoringStrategy scoringStrategy : scoringStrategies) {
             if(scoringStrategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)){
                 ScoringStrategyConfig scoringStrategyConfig = scoringStrategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                 if(scoringStrategyConfig.appType()==app.getAppType()&&scoringStrategyConfig.scoringStrategy()== app.getScoringStrategy()) {
                     return scoringStrategy.doScore(app, choices);
                 }
             }
         }
         throw new BusinessException(ErrorCode.OPERATION_ERROR,"没有找到对应的评分策略");
    }
}
