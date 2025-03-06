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
import java.util.List;

@ScoringStrategyConfig(appType = 0,scoringStrategy = 0)
public class CustomScoreScoringStartegy implements ScoringStrategy{
    @Resource
    QuestionService questionService;
    @Resource
    ScoringResultService scoringResultService;
    @Override
    public UserAnswer doScore(App app, List<String> choices) {
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, app.getId()));
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, app.getId()).orderByDesc(ScoringResult::getResultScoreRange));

        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(question.getQuestionContent(), QuestionContentDTO.class);

        Integer resultScore = 0;
        for (int i = 0; i < choices.size(); i++) {
            List<QuestionContentDTO.Option> options = questionContentDTOList.get(i).getOptions();
            for (QuestionContentDTO.Option option : options) {
                if (option.getKey().equals(choices.get(i))) {
                    resultScore+= Integer.valueOf(option.getResult());
                }
            }
        }


        ScoringResult scoringResult=new ScoringResult();
        for (ScoringResult result : scoringResultList) {
            if (resultScore>=result.getResultScoreRange()) {
                scoringResult=result;
                break;
            }
        }
        UserAnswer userAnswer=new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(scoringResult.getId());
        userAnswer.setResultName(scoringResult.getResultName());
        userAnswer.setResultDesc(scoringResult.getResultDesc());
        userAnswer.setResultPicture(scoringResult.getResultPicture());
        return null;
    }
}
