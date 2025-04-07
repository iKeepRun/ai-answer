package com.zack.aianswer.scoring;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zack.aianswer.common.ErrorCode;
import com.zack.aianswer.constant.AiPromptConstant;
import com.zack.aianswer.exception.BusinessException;
import com.zack.aianswer.manager.AiManager;
import com.zack.aianswer.model.dto.question.QuestionAnswerDTO;
import com.zack.aianswer.model.dto.question.QuestionContentDTO;
import com.zack.aianswer.model.entity.App;
import com.zack.aianswer.model.entity.Question;
import com.zack.aianswer.model.entity.UserAnswer;
import com.zack.aianswer.service.QuestionService;
import net.sf.jsqlparser.statement.select.KSQLWindow;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;


import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AiTestScoringStrategy implements ScoringStrategy {
    @Resource
    QuestionService questionService;
    @Resource
    AiManager aiManager;
    @Resource
    RedissonClient redissonClient;
    //从缓存中获取回答
    private final Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(1024)
            .expireAfterAccess(5L, TimeUnit.MINUTES)
            .build();


    private final String lock = "AI_ANSWER_LOCK";

    @Override
    public UserAnswer doScore(App app, List<String> choices) {
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, app.getId()));
        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(question.getQuestionContent(), QuestionContentDTO.class);
        // 从缓存中获取值
        String key = createKey(String.valueOf(app.getId()), choices);
        String answer_cache = cache.getIfPresent(key);
        if (StrUtil.isNotBlank(answer_cache)) {
            UserAnswer userAnswerNew = JSONUtil.toBean(answer_cache, UserAnswer.class);
            return userAnswerNew;
        }
        //使用redisson加锁
        //定义锁
        RLock redissonClientLock = redissonClient.getLock(lock + key);

        try {
            //竞争锁
            boolean tryLock = redissonClientLock.tryLock(3, 15, TimeUnit.SECONDS);
            if (!tryLock) {
                return null;
            }
            //抢到锁
            List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
            for (int i = 0; i < choices.size(); i++) {
                //新建题目答案对象
                QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
                //当前题目
                QuestionContentDTO questionContentDTO = questionContentDTOList.get(i);
                questionAnswerDTO.setTitle(questionContentDTO.getTitle());
                //从当前题目中获取选项对应的value
                List<QuestionContentDTO.Option> optionList = questionContentDTO.getOptions();
                for (QuestionContentDTO.Option option : optionList) {
                    if (option.getKey().equals(choices.get(i))) {
                        questionAnswerDTO.setAnswer(option.getValue());
                    }
                }

                questionAnswerDTOList.add(questionAnswerDTO);
            }
            //拼接用户信息
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(app.getAppName() + "\n");
            stringBuilder.append(app.getAppDesc() + "\n");
            stringBuilder.append(JSONUtil.toJsonStr(questionAnswerDTOList));

            String stableAnswer = aiManager.doSyncStableRequest(AiPromptConstant.AI_CORRECT_QUESTION_PROMPT, stringBuilder.toString());
            int start = stableAnswer.indexOf("{");
            int end = stableAnswer.lastIndexOf("}");
            String substring = stableAnswer.substring(start, end + 1);
            JSONObject result = JSONUtil.parseObj(substring);

            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setAppId(app.getId());
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JSONUtil.toJsonStr(choices));
            userAnswer.setResultId(IdUtil.getSnowflakeNextId());
            userAnswer.setResultName((String) result.get("resultName"));
            userAnswer.setResultDesc((String) result.get("resultDesc"));

            //添加本地缓存
            cache.put(key, JSONUtil.toJsonStr(userAnswer));
            return userAnswer;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI回答异常");
        } finally {
            if (redissonClientLock != null && redissonClientLock.isLocked()) {
                if (redissonClientLock.isHeldByCurrentThread()) {
                    redissonClientLock.unlock();
                }
            }
        }
    }


    private String createKey(String appId, List<String> choices) {
        return appId + ":" + DigestUtil.md5Hex(JSONUtil.toJsonStr(choices));
    }

}