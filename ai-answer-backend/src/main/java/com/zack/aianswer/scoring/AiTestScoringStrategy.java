package com.zack.aianswer.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zack.aianswer.model.dto.question.QuestionContentDTO;
import com.zack.aianswer.model.entity.App;
import com.zack.aianswer.model.entity.Question;
import com.zack.aianswer.model.entity.ScoringResult;
import com.zack.aianswer.model.entity.UserAnswer;
import com.zack.aianswer.service.QuestionService;
import com.zack.aianswer.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScoringStrategyConfig(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {
    @Resource
    QuestionService questionService;
    @Resource
    ScoringResultService scoringResultService;


    @Override
    public UserAnswer doScore(App app, List<String> choices) {
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, app.getId()));

        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(question.getQuestionContent(), QuestionContentDTO.class);

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < choices.size(); i++) {
            List<QuestionContentDTO.Option> options = questionContentDTOList.get(i).getOptions();
            for (QuestionContentDTO.Option option : options) {
                if (option.getKey().equals(choices.get(i))) {
                   if(map.containsKey(option.getResult())){
                       map.put(option.getResult(), (Integer) map.get(option.getResult()) + 1);
                    }else{
                        map.put(option.getResult(), 1);
                    }
                }
            }
        }

        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, app.getId()));
        Integer maxScore=0;

        ScoringResult resultScoringResult=scoringResultList.get(0);

        for (ScoringResult scoringResult : scoringResultList) {
            List<String> propList = JSONUtil.toList(scoringResult.getResultProp(), String.class);
            int score = propList.stream().mapToInt(prop -> map.get(prop) == null ? 0 : (Integer) map.get(prop)).sum();

            if(score>maxScore){
               resultScoringResult=scoringResult;
               maxScore=score;
            }
        }

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(resultScoringResult.getId());
        userAnswer.setResultName(resultScoringResult.getResultName());
        userAnswer.setResultDesc(resultScoringResult.getResultDesc());
        userAnswer.setResultPicture(resultScoringResult.getResultPicture());

        return userAnswer;
    }
}
