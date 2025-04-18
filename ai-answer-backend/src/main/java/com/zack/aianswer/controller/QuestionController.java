package com.zack.aianswer.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zack.aianswer.annotation.AuthCheck;
import com.zack.aianswer.common.BaseResponse;
import com.zack.aianswer.common.DeleteRequest;
import com.zack.aianswer.common.ErrorCode;
import com.zack.aianswer.common.ResultUtils;
import com.zack.aianswer.constant.AiPromptConstant;
import com.zack.aianswer.constant.UserConstant;
import com.zack.aianswer.exception.BusinessException;
import com.zack.aianswer.exception.ThrowUtils;
import com.zack.aianswer.manager.AiManager;
import com.zack.aianswer.model.dto.question.*;
import com.zack.aianswer.model.entity.App;
import com.zack.aianswer.model.entity.Question;
import com.zack.aianswer.model.entity.User;
import com.zack.aianswer.model.vo.QuestionVO;
import com.zack.aianswer.service.AppService;
import com.zack.aianswer.service.QuestionService;
import com.zack.aianswer.service.UserService;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 题目接口
 *
 * @author <a href="https://github.com/iKeepRun">zack</a>
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {
    @Resource
    private AiManager aiManager;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建题目
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<QuestionContentDTO> questionContent = questionAddRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContent));

        // 数据校验
        questionService.validQuestion(question, true);
        //填充默认值
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除题目
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题目（仅管理员可用）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<QuestionContentDTO> questionContent = questionUpdateRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContent));
        // 数据校验
        questionService.validQuestion(question, false);
        // 判断是否存在
        long id = questionUpdateRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页获取题目列表（仅管理员可用）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取题目列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前登录用户创建的题目列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 编辑题目（给用户使用）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<QuestionContentDTO> questionContent = questionEditRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContent));
        // 数据校验
        questionService.validQuestion(question, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = questionEditRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    //ai 生成题目

    @PostMapping("/ai/generate")
    public BaseResponse<List<QuestionContentDTO>> aiCreateQuestion(@RequestBody QuestionAiCreateRequest questionAiCreateRequest) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(questionAiCreateRequest.getAppName() + "\n");
            stringBuilder.append(questionAiCreateRequest.getAppDesc() + "\n");
            stringBuilder.append(questionAiCreateRequest.getAppType() + "\n");
            stringBuilder.append(questionAiCreateRequest.getQuestionNum() + "\n");
            stringBuilder.append(questionAiCreateRequest.getOptionNum());
            String syncUnStableAnswer = aiManager.doSyncUnStableRequest(AiPromptConstant.AI_SET_QUESTION_PROMPT, stringBuilder.toString());
            int start = syncUnStableAnswer.indexOf("[");
            int end = syncUnStableAnswer.lastIndexOf("]");
            String substring = syncUnStableAnswer.substring(start, end + 1);
            return ResultUtils.success(JSONUtil.toList(substring, QuestionContentDTO.class));
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR.getCode(), e.getMessage());
        }
    }


    @GetMapping("/ai/generate/sse")
    public SseEmitter aiCreateQuestionSSE(QuestionAiCreateRequest questionAiCreateRequest) {
        ThrowUtils.throwIf(questionAiCreateRequest == null, ErrorCode.PARAMS_ERROR);
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0l);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(questionAiCreateRequest.getAppName() + "\n");
        stringBuilder.append(questionAiCreateRequest.getAppDesc() + "\n");
        stringBuilder.append(questionAiCreateRequest.getAppType() + "\n");
        stringBuilder.append(questionAiCreateRequest.getQuestionNum() + "\n");
        stringBuilder.append(questionAiCreateRequest.getOptionNum());
        Flowable<ModelData> modelDataFlowable = aiManager.doStreamRequest(AiPromptConstant.AI_SET_QUESTION_PROMPT, stringBuilder.toString(), null);
        StringBuilder contentBuilder = new StringBuilder();
        AtomicInteger flag = new AtomicInteger(0);
        modelDataFlowable
                .subscribeOn(Schedulers.io())
                // 异步线程池执行
                .observeOn(Schedulers.computation())
                .map(chunk -> chunk.getChoices().get(0).getDelta().getContent().replaceAll("\\s", ""))
                .filter(StrUtil::isNotBlank)
                .observeOn(Schedulers.newThread())
                .doOnNext(content -> {
                    synchronized (contentBuilder) {
                        for (int i = 0; i < content.length(); i++) {
                            char c = content.charAt(i);
                            // 识别第一个 [ 表示开始 AI 传输 json 数据，打开 flag 开始拼接 json 数组
                            if (c == '{') {
                                flag.addAndGet(1);
                            }
                            if (flag.get() > 0) {
                                contentBuilder.append(c);
                            }
                            if (c == '}') {
                                flag.addAndGet(-1);
                                if (flag.get() == 0) {
                                    // 累积单套题目满足 json 格式后，sse 推送至前端
                                    // sse 需要压缩成当行 json，sse 无法识别换行
                                    sseEmitter.send(JSONUtil.toJsonStr(contentBuilder.toString()));
                                    // 清空 StringBuilder
                                    contentBuilder.setLength(0);
                                }
                            }
                        }
                    }
                }).doOnComplete(() -> {
                    try {
                        sseEmitter.complete();
                    } catch (Exception e) {
                        log.error("sseEmitter complete error", e);
                    }
                }).
                doOnError(e -> {
                    log.error("Stream error", e);
                    sseEmitter.completeWithError(e);
                }).subscribe();
        return sseEmitter;
    }
    // endregion
}
