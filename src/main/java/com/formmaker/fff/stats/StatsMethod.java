package com.formmaker.fff.stats;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.SatisfactionResponse;
import com.formmaker.fff.stats.dto.SelectResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatsMethod {

    static public StatsMethod statsMethod = new StatsMethod();


    public QuestionStats statsChoice(List<Reply> replyList, Question question) {


        List<SelectResponse> selectList = new ArrayList<>();
        SelectResponse selectResponse;
        for (Answer answer : question.getAnswerList()) {
            selectResponse = new SelectResponse(answer.getAnswerValue(), answer.getAnswerNum());
            selectList.add(selectResponse);
        } //해당 질문과 관련된 질문 문항과 문항 번호들을 selectList 담아온다.

        selectList = selectList.stream()
                .sorted(Comparator.comparing(SelectResponse::getAnswerNum)).collect(Collectors.toList());
        //문항 번호별로 정렬해준다.
        int totalSelect = 0;


        for (Reply userReply : replyList) {
            List<String> selectValueList = List.of(userReply.getSelectValue().split("\\|"));  //유저들의 문항선택 값을 List에 넣어주고

            List<Integer> changeSelectValueList = selectValueList.stream() //스트링 리스트를 Integer 리스트로 변환
                    .map(Integer::parseInt).toList();


            for (Integer selectValues : changeSelectValueList) { // 유저들의 문항선택 값 리스트 값들을 selectValues 하나하나 넣어주고
                selectList.get(selectValues - 1).increaseValue(); //번호별로 정렬된 문항에서 유저가 문항선택한 값 들이 나올 때마다 해당 문항의 벨류는 증가하고
                totalSelect++; // 해당 Question의 전체 선택 횟수가 증가한다.
            }
        }
        for (SelectResponse select : selectList) {
            select.valueAvg(totalSelect); //문항별로 Value= 백분율 통계를 내준다.
        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .selectList(selectList)
                .build();
    }

    public QuestionStats statsMultipleChoice(List<Reply> replyList, Question question) {

        return QuestionStats.builder().build();
    }



    public QuestionStats statsSlide(List<Reply> replyList, Question question) {

        List<SatisfactionResponse> satisfactionList = new ArrayList<>();
        SatisfactionResponse satisfactionResponse;
            int volume = question.getVolume();


        for(int x = -volume ; x<=volume; x++){
           int indexNum = x+volume;
           int value = indexNum-volume;
           satisfactionResponse = new SatisfactionResponse(value);
           satisfactionList.add(satisfactionResponse);
        }
        satisfactionList = satisfactionList.stream()
                .sorted(Comparator.comparing(SatisfactionResponse::getValue)).collect(Collectors.toList());

        int totalSelect = 0 ;

        for (Reply userReply : replyList) {
            List<String> satisfactionValueList = List.of(userReply.getSelectValue());  //유저들의 선택 값을 List에 넣어주고
            List<Integer> changeSatisfactionValueList = satisfactionValueList.stream() //스트링 리스트를 Integer 리스트로 변환
                    .map(Integer::parseInt).toList();

            for (Integer satisfactionValues : changeSatisfactionValueList) { // 유저들의 만족도선택 값 리스트 값들을 satisfactionValues 하나하나 넣어주고
                satisfactionList.get(satisfactionValues + volume).increaseValue(); //번호별로 정렬된 만족도에서 유저가 만족도를 선택한 값 들이 나올 때마다 해당 문항의 벨류는 증가하고
                totalSelect++;
            }
        }
        for (SatisfactionResponse satisfaction : satisfactionList) {
            satisfaction.valueAvg(totalSelect); //문항별로 Value= 백분율 통계를 내준다.
        }
        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .volume(question.getVolume())
                .satisfactionList(satisfactionList)
                .build();
    }

    public QuestionStats statsRank(List<Reply> replyList, Question question) {


        //문항 내 폼 1개당 점수 반영 3개면 1명이 3개 반영
        //각 폼에 대해서 합계를 낸 다음에
        //제일 높은 점수를 가진 폼을 필두로 오름차순 , 및 합계치 퍼센테이지로 변환해서 데이터 반환 ?

        return QuestionStats.builder().build();
    }

    public QuestionStats statsLongDescriptive(List<Reply> replyList, Question question) {

        return QuestionStats.builder().build();
    }

    public QuestionStats statsShortDescriptive(List<Reply> replyList, Question question) {

        return QuestionStats.builder().build();
    }

    // 별점, 스코어 통계 처리 로직
    public QuestionStats statsOfPositiveValue(List<Reply> replyList, Question question) {

        List<Integer> valueList = new ArrayList<>();

        for (Reply reply : replyList) {
            Integer value = Integer.parseInt(reply.getSelectValue());
            valueList.add(value);
        }

        // 평균을 구하는 로직
        int participant = valueList.size();
        int sum = valueList.stream().mapToInt(Integer::intValue).sum();
        float average = (float) sum / participant;
        Float avg = Float.valueOf(String.format("%1f", average));

        // 각 문항이 몇 번 선택받았는지 출력하는 로직
        int selectOne = Collections.frequency(valueList, 1);
        int selectTwo = Collections.frequency(valueList, 2);
        int selectThree = Collections.frequency(valueList, 3);
        int selectFour = Collections.frequency(valueList, 4);
        int selectFive = Collections.frequency(valueList, 5);

        // 각 문항의 선택률(?)
        float rateOne = (float) selectOne / participant;
        float rateTwo = (float) selectTwo / participant;
        float rateThree = (float) selectThree / participant;
        float rateFour = (float) selectFour / participant;
        float rateFive = (float) selectFive / participant;

        List<Float> selectRate = new ArrayList<>();

        selectRate.add(rateOne);
        selectRate.add(rateTwo);
        selectRate.add(rateThree);
        selectRate.add(rateFour);
        selectRate.add(rateFive);

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .questionAvg(avg)
                .satisfactionList(selectRate)

                .build();
    }

    /**
     * String 타입으로 저장된 Json 형식 문자열을 JsonObject
     * 로 사용할 수 있게 만들어주는 로직
     * JsonObject 는 HashMap 을 상속받는 클래스이다. Key, Value 로 값에 접근할 수 있다.
     */
    private JSONObject toJsonObjectType(String selectValueToStringTypeJsonForm) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(selectValueToStringTypeJsonForm);
        } catch (ParseException e) {
            /**
             * 에러 메세지는 로그를 통해 남기는 방식을 채택 -> 로그 남기는 방법 알아봐야함.
             * 에러가 터져서 멈추는게 아닌, 에러가 터진 데이터를 제외한 통계를 반환시켜주어야함.
             * new CustomException(ErrorCode.INVALID_FORM_DATA);
             */

        }
        return new JSONObject();
    }
}
