package com.formmaker.fff.survey.repository;


import com.formmaker.fff.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Page<Survey> findByUserId(Long userId, Pageable pageable);
}
