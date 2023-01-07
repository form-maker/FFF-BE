package com.formmaker.fff.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {


    void deleteAllByQuestionId(Long id);
}
