package com.formmaker.fff.survey;

import com.formmaker.fff.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
