package com.formmaker.fff.question;

import com.formmaker.fff.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);

    List<Question> findAllBySurveyId(Long surveyId);

}
