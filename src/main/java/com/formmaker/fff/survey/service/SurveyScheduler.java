package com.formmaker.fff.survey.service;

import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SurveyScheduler {

    private final SurveyRepository surveyRepository;

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduleTaskUsingCronExpression() {
        LocalDate today = LocalDate.now();

        /*
        //이후 마감 관련 알람시 사용
        List<Survey> surveyList = surveyRepository.findAllByEndedAt(today);
        */

        surveyRepository.updateStartSurvey(StatusTypeEnum.IN_PROCEED, today);
        surveyRepository.updateEndSurvey(StatusTypeEnum.DONE, today);
        surveyRepository.updateDDay(StatusTypeEnum.IN_PROCEED);

    }
}
