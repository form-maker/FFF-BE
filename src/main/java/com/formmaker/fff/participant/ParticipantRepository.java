package com.formmaker.fff.participant;

import com.formmaker.fff.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllBySurvey(Survey survey);

    Optional<Participant> findBySurveyAndLoginId(Survey survey, String loginId);


    Optional<Participant> findByLoginId(String loginId);

    @Transactional
    @Modifying
    @Query("delete from Participant q where q.survey = :ids")
    void deleteAllBySurveyId(@Param("ids")Survey survey);

}
