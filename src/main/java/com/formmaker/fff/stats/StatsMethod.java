package com.formmaker.fff.stats;


import com.formmaker.fff.answer.dto.AnswerDto;
import com.formmaker.fff.question.dto.QuestionDto;
import com.formmaker.fff.reply.dto.request.ReplyDto;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.SelectResponse;
import org.hibernate.sql.SelectValues;

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
            selectList.add(selectResponse);
        } //해당 질문과 관련된 질문 문항과 문항 번호들을 selectList 담아온다.

        selectList = selectList.stream()
                    .sorted(Comparator.comparing(SelectResponse::getAnswerNum)).collect(Collectors.toList());
        //문항 번호별로 정렬해준다.
        int totalSelect = 0;


        for(ReplyDto userReply : replyDtoList){
            List<String> selectValueList = List.of(userReply.getSelectValue().split("|"));  //유저들의 문항선택 값을 List에 넣어주고
            List<Integer> changeSelectValueList = selectValueList.stream() //스트링 리스트를 Integer 리스트로 변환
                    .map(Integer::parseInt).toList();


            for (Integer selectValues : changeSelectValueList) { // 유저들의 문항선택 값 리스트 값들을 selectValues 하나하나 넣어주고
                selectList.get(selectValues-1).increaseValue(); //번호별로 정렬된 문항에서 유저가 문항선택한 값 들이 나올 때마다 해당 문항의 벨류는 증가하고
                totalSelect++; // 해당 Question의 전체 선택 횟수가 증가한다.
            }
        }
        for(SelectResponse select : selectList) {
            select.valueAvg(totalSelect); //문항별로 Value= 백분율 통계를 내준다.
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
