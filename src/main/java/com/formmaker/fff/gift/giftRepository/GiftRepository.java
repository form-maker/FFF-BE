package com.formmaker.fff.gift.giftRepository;

import com.formmaker.fff.gift.entity.Gift;
import com.formmaker.fff.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GiftRepository extends JpaRepository<Gift, Long> {

    @Transactional
    @Modifying
    @Query("delete from Gift q where q.survey = :ids")
    void deleteAllBySurveyId(@Param("ids") Survey survey);
}
