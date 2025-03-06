package com.zack.aianswer.scoring;

import com.zack.aianswer.model.entity.App;
import com.zack.aianswer.model.entity.UserAnswer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScoringStrategy {
    UserAnswer doScore(App app, List<String> choices);
}
