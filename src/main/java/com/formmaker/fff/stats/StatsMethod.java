package com.formmaker.fff.stats;


import com.formmaker.fff.answer.dto.AnswerDto;
import com.formmaker.fff.question.dto.QuestionDto;
import com.formmaker.fff.reply.dto.ReplyDto;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.SelectResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatsMethod {

    static public StatsMethod statsMethod = new StatsMethod();


    public QuestionStats statsSingleChoice(List<ReplyDto> replyDtoList, QuestionDto questionDto){

        List<SelectResponse> selectList = new ArrayList<>();
        SelectResponse selectResponse;
        for(AnswerDto answerDto : questionDto.getAnswerList()){
            selectResponse = new SelectResponse(answerDto.getAnswerValue(), answerDto.getAnswerNum());
            selectList.add(selectResponse); //리스트에 넣다.
        }

        selectList = selectList.stream()
                .sorted(Comparator.comparing(SelectResponse::getAnswerNum)).collect(Collectors.toList());

        for(ReplyDto userReply : replyDtoList){
            Integer selectValue = Integer.parseInt(userReply.getSelectValue()); //선택한 응답에 벨류 -> 1/14 (형준 수정) 확인 부탁드립니다.

            selectList.get(selectValue-1).increaseValue();
        }
       return  QuestionStats.builder()
                .questionNum(questionDto.getQuestionNum())
                .questionType(questionDto.getQuestionType())
                .questionTitle(questionDto.getTitle())
                .questionSummary(questionDto.getSummary())
                .selectList(selectList)
                .build();



    }
    public QuestionStats statsMultipleChoice(List<ReplyDto> replyDtoList, QuestionDto questionDto){

        return QuestionStats.builder().build();
    }
    public QuestionStats statsSlide(List<ReplyDto> replyDtoList, QuestionDto questionDto){

        return QuestionStats.builder().build();
    }
    public QuestionStats statsRank(List<ReplyDto> replyDtoList, QuestionDto questionDto){
        //문항 내 폼 1개당 점수 반영 3개면 1명이 3개 반영
        //각 폼에 대해서 합계를 낸 다음에
        //제일 높은 점수를 가진 폼을 필두로 오름차순 , 및 합계치 퍼센테이지로 변환해서 데이터 반환 ?
        return QuestionStats.builder().build();
    }
    public QuestionStats statsLongDescriptive(List<ReplyDto> replyDtoList, QuestionDto questionDto){

        return QuestionStats.builder().build();
    }
    public QuestionStats statsShortDescriptive(List<ReplyDto> replyDtoList, QuestionDto questionDto){

        return QuestionStats.builder().build();
    }
    public QuestionStats statsStar(List<ReplyDto> replyDtoList, QuestionDto questionDto){

        return QuestionStats.builder().build();
    }
    public QuestionStats statsScore(List<ReplyDto> replyDtoList, QuestionDto questionDto){

        return QuestionStats.builder().build();
    }
}
