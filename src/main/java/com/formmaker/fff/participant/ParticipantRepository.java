package com.formmaker.fff.participant;

import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllBySurvey(Survey survey);

    Optional<Participant> findBySurveyAndUser(Survey survey, User user);
}
