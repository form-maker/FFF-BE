package com.formmaker.fff.answer.repositoy;

import com.formmaker.fff.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnswerRepository extends JpaRepository<Answer, Long> {

    void deleteAllByQuestionId(Long id);

}
