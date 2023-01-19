package com.formmaker.fff.answer.repositoy;

import com.formmaker.fff.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Transactional
    @Modifying
    @Query("delete from Answer a where a.questionId in :ids")
    void deleteAllByQuestionIdIn(@Param("ids") List<Long> ids);
}
