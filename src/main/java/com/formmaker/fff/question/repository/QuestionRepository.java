package com.formmaker.fff.question.repository;

import com.formmaker.fff.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    void deleteAllBySurveyId(Long surveyId);

    Question findAllById(Long questionId);
}
