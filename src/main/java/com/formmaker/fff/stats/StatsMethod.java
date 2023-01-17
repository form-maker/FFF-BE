package com.formmaker.fff.stats;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.stats.dto.DescriptiveResponse;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.SelectResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        selectList.stream().map(select -> select.getValue()).collect(Collectors.toList());
        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .selectList(selectList.stream().map(select -> select.getValue()).collect(Collectors.toList()))
                .build();
    }


    public QuestionStats statsSlide(List<Reply> replyList, Question question) {

        List<SelectResponse> satisfactionList = new ArrayList<>();
        SelectResponse selectResponse;
        int volume = question.getVolume();


        for (int x = -volume; x <= volume; x++) {
            int indexNum = x + volume;
            int value = indexNum - volume;
            selectResponse = new SelectResponse(value);
            satisfactionList.add(selectResponse);
        }
        satisfactionList = satisfactionList.stream()
                .sorted(Comparator.comparing(SelectResponse::getChoiceValue)).collect(Collectors.toList());

        int totalSelect = 0;

        for (Reply userReply : replyList) {
            List<String> satisfactionValueList = List.of(userReply.getSelectValue());  //유저들의 선택 값을 List에 넣어주고
            List<Integer> changeSatisfactionValueList = satisfactionValueList.stream() //스트링 리스트를 Integer 리스트로 변환
                    .map(Integer::parseInt).toList();

            for (Integer satisfactionValues : changeSatisfactionValueList) { // 유저들의 만족도선택 값 리스트 값들을 satisfactionValues 하나하나 넣어주고
                satisfactionList.get(satisfactionValues + volume).increaseValue(); //번호별로 정렬된 만족도에서 유저가 만족도를 선택한 값 들이 나올 때마다 해당 문항의 벨류는 증가하고
                totalSelect++;
            }
        }
        for (SelectResponse satisfaction : satisfactionList) {
            satisfaction.valueAvg(totalSelect); //문항별로 Value= 백분율 통계를 내준다.
        }
        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .volume(question.getVolume())
                .satisfactionList(satisfactionList.stream().map(satisfaction -> satisfaction.getValue()).collect(Collectors.toList()))
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
        ArrayList<String> shortDescriptiveList = new ArrayList<>();
        ArrayList<DescriptiveResponse> descriptiveList = new ArrayList<>();
        DescriptiveResponse descriptiveResponse;

        for (Reply userReply : replyList) { //해당 설문에 유저들이 입력한 단답형을 가져온다 .
            shortDescriptiveList.add(String.valueOf(userReply.getDescriptive()));
        }

        Map<String,Integer> map = new HashMap<String,Integer>();
        for(String str : shortDescriptiveList){
            Integer count = map.get(str);
            if(count==null){
                map.put(str,1);
            }else{
                map.put(str,count + 1 );
            }
        }
        for(String key : map.keySet()){
            String a = (key);
            Integer b = (map.get(key));
            descriptiveResponse = new DescriptiveResponse(a,b);
            descriptiveList.add(descriptiveResponse);
        }
//        Set<String> set = new HashSet<String>(shortDescriptiveList); //비교자료로 정의한다.
//
//        for (String str : set) {
//            response =(str + ":" + Collections.frequency(shortDescriptiveList, str));
//
//
//            descriptiveList.add(response);
//        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .descriptiveList(descriptiveList)
                .build();
    }

    // 별점, 스코어 통계 처리 로직
    public QuestionStats statsOfPositiveValue(List<Reply> replyList, Question question) {


        return QuestionStats.builder()
                .questionNum(null)
                .questionType(null)
                .questionTitle(null)
                .questionSummary(null)
                .questionAvg(null)
                .satisfactionList(null)
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
