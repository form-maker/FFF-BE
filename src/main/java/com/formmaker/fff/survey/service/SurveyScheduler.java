package com.formmaker.fff.survey.service;

import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class SurveyScheduler {

    private final SurveyRepository surveyRepository;

    @Scheduled(cron = "0 0 14 * * *")
    public void scheduleTaskUsingCronExpression() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info(yesterday.toString());

        log.info(today+" 설문 상태 업데이트");

        /* 설문 시작 */
        surveyRepository.updateSurvey(StatusTypeEnum.IN_PROCEED, today, StatusTypeEnum.DELETE);

        /* 설문 종료 */
        surveyRepository.updateSurvey(StatusTypeEnum.DONE, today, StatusTypeEnum.DELETE);

    }
}
