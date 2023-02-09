package com.formmaker.fff.survey.repository;


import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Optional<Survey> findByIdAndStatusNot(Long id, StatusTypeEnum status);

    Optional<Survey> findByIdAndStatus(Long id, StatusTypeEnum status);


    Page<Survey> findByUserIdAndStatus(Long userId, StatusTypeEnum status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Survey s set s.status = :done where s.endedAt = :today and s.status <> :delete")
    void updateSurvey(@Param("done") StatusTypeEnum done, @Param("today") LocalDate today, @Param("delete") StatusTypeEnum delete);

    Page<Survey> findAllByStatus(Pageable pageable, StatusTypeEnum inProceed);

    @Query("select u.email from User u inner join Survey s " +
            "on not s.status = :delete " +
            "and s.endedAt = :minusDays " +
            "and s.user.id = u.id")
    List<String> findAllEndedSurveyUserEmail(LocalDate minusDays, StatusTypeEnum delete);
}
