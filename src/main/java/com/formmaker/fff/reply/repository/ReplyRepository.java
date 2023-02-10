package com.formmaker.fff.reply.repository;

import com.formmaker.fff.participant.Participant;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByQuestionIdAndCreatedAtAfterAndCreatedAtBefore(Long questionId, LocalDateTime start, LocalDateTime end);

    List<Reply> findAllByParticipant(Participant participant);

    @Transactional
    @Modifying
    @Query("delete from Reply a where a.questionId in :ids")
    void deleteAllByQuestionIdIn(@Param("ids") List<Long> ids);


    @Query(value = "select * from fff.reply r join fff.participant as p on r.participant_id = p.id where p.survey_id = :surveyId", nativeQuery = true)
    List<Reply> findBySurveyId(@Param("surveyId") Long surveyId);

}
