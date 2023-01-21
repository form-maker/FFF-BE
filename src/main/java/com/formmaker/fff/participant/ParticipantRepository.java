package com.formmaker.fff.participant;

import com.formmaker.fff.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllBySurvey(Survey survey);
}
