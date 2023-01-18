package com.formmaker.fff.stats.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DailyParticipant {
    private List<LocalDate> date = new ArrayList<>();
    private List<Integer> participant = new ArrayList<>();

    public void addDate(LocalDate day){
        this.date.add(day);
    }

    public void addParticipant(Integer participant){
        this.participant.add(participant);
    }
}
