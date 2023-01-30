package com.formmaker.fff.gift.entity;

import com.formmaker.fff.survey.entity.Survey;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String giftName;
    @Column(nullable = false)
    private Integer giftQuantity;
    @Column(nullable = false)
    private String giftIcon;
    @Column(nullable = false)
    private String giftSummary;
    @ManyToOne
    @JoinColumn(name = "surveyId")
    private Survey survey;

    @Builder
    public Gift(String giftName, Integer giftQuantity, String giftIcon, String giftSummary, Survey survey) {
        this.giftName = giftName;
        this.giftQuantity = giftQuantity;
        this.giftIcon = giftIcon;
        this.giftSummary = giftSummary;
        this.survey = survey;
    }
}
