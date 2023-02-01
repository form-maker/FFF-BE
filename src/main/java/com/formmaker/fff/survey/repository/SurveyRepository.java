package com.formmaker.fff.survey.repository;


import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Page<Survey> findByUserId(Long userId, Pageable pageable);

    Optional<Survey> findByIdAndStatusNot(Long id, StatusTypeEnum status);

    Optional<Survey> findByIdAndStatus(Long id, StatusTypeEnum status);


    Page<Survey> findByUserIdAndStatus(Long userId, StatusTypeEnum status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Survey s set s.status = :start where s.startedAt = :today")
    void updateStartSurvey(StatusTypeEnum start, LocalDate today);

    @Modifying
    @Transactional
    @Query("update Survey s set s.status = :done where s.endedAt = :yesterday")
    void updateEndSurvey(StatusTypeEnum done, LocalDate yesterday);

    Page<Survey> findAllByStatus(Pageable pageable, StatusTypeEnum inProceed);

    Optional<Survey> findByIdAndUserId(Long surveyId, Long userId);

    List<Survey> findAllByEndedAtAndStatusNot(LocalDate minusDays, StatusTypeEnum delete);
}
