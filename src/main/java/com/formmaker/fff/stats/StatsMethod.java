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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsMethod {

    static public StatsMethod statsMethod = new StatsMethod();

    public QuestionStats statsChoice(List<Reply> replyList, Question question) {

        List<SelectResponse> selectList = question.getAnswerList().stream().map(answer -> new SelectResponse(answer.getAnswerValue(), answer.getAnswerNum())).collect(Collectors.toList());

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
            satisfactionList.get(Integer.parseInt(userReply.getSelectValue()) + volume).increaseValue();
            totalSelect++;
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
        /*
            모든 랭크 타입의 응답을 List 로 저장 단일 요소의 형태
            -> {Key:Value, Key:Value, Key:Value ...}
         */
        List<JSONObject> rankObjectList = new ArrayList<>();
        for (Reply reply : replyList) {
            JSONObject rankObject = toJsonObjectType(reply.getSelectValue());
            rankObjectList.add(rankObject);
        }

        /*
            rankObject 는 key, value 값으로 구성되어 있으며,
            key 값으로 정렬되어 있다.
            value 로만 구성된 List 를 생성하기 위한 로직 (List<Integer>)
         */
        List<List<Integer>> selectValueList = new ArrayList<>();
        for (JSONObject rankObject : rankObjectList) {
            List<Integer> selectValue = new ArrayList<>();
            for (int answerNum = 0; answerNum < rankObject.size(); answerNum++) {
                selectValue = new ArrayList<>(rankObject.values());
            }
            selectValueList.add(selectValue);
        }

        /*
            selectValue 는 index 가 answer 의 번호(number)를 나타내며,
            value 는 문항에 대해 응답자가 지정한 순위를 나타낸다.
            각각의 문항에 대해 응답자들이 지정한 순위를 담은 List 를 생성하기 위한 로직
            ex -> valuesOfAnswers 의 0 번 index 에 있는 리스트는 1번 문항에 대해 응답자들이 지정한 순위를 나열
         */
        List<List<Integer>> valuesOfAnswers = new ArrayList<>();
        if(!selectValueList.isEmpty()){
            for (int i = 0; i < selectValueList.get(0).size(); i++) {
                List<Integer> eachValuesOfAnswer = new ArrayList<>();
                for (int j = 0; j < selectValueList.size(); j++) {
                    eachValuesOfAnswer.add(Integer.parseInt("" + selectValueList.get(j).get(i)));
//                eachValuesOfAnswer.add(selectValueList.get(j).get(i));
                }
                valuesOfAnswers.add(eachValuesOfAnswer);
            }
        }



        /*
            각 문항에 대해 응답자들이 지정한 순위가 나열된 List 를 가지고,
            지정 순위의 선택 비율을 계산
         */
        List<SelectResponse> selectResponseList = new ArrayList<>();
        List<Float> rankList = new ArrayList<>();
        int answerNum = 0;
        for (List<Integer> valuesOfAnswer : valuesOfAnswers) {
            List<Integer> selectCountList = new ArrayList<>();
            for (int i = 0; i < valuesOfAnswers.size(); i++) {
                selectCountList.add(Collections.frequency(valuesOfAnswer, (i + 1)));
            }
            rankList = selectAvg(selectCountList);

            // 각 문항에 값 넣어주기
            List<Answer> answerList = question.getAnswerList();
            SelectResponse selectResponse = new SelectResponse(answerList.get(answerNum++).getAnswerValue(), rankList);
            selectResponseList.add(selectResponse);
        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .selectList(selectResponseList)
                .build();
    }

    public QuestionStats statsLongDescriptive(List<Reply> replyList, Question question) {
        List<DescriptiveResponse> descriptiveList = new ArrayList<>();
        int randomValue;
        for(int i = 0; i < Math.min(3, replyList.size()); i++){
            randomValue = (int)(Math.random() * 100)%replyList.size();
            descriptiveList.add(new DescriptiveResponse(replyList.get(randomValue).getDescriptive()));
        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .descriptiveList(descriptiveList)
                .build();
    }


    public QuestionStats statsShortDescriptive(List<Reply> replyList, Question question) {

        ArrayList<DescriptiveResponse> descriptiveList = new ArrayList<>();

        List<String> shortDescriptiveList = replyList.stream().map(Reply::getDescriptive).toList();

        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String str : shortDescriptiveList) {
            Integer count = map.get(str);
            if (count == null) {
                map.put(str, 1);
            } else {
                map.put(str, count + 1);
            }
        }
        for (String key : map.keySet()) {
            descriptiveList.add(new DescriptiveResponse(key, map.get(key)));
        }

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

        List<Integer> valueList = new ArrayList<>();

        for (Reply reply : replyList) {
            Integer value = Integer.parseInt(reply.getSelectValue());
            valueList.add(value);
        }

        // 평균을 구하는 로직
        int participant = valueList.size();
        int sum = valueList.stream().mapToInt(Integer::intValue).sum();
        float avg = participant!=0?Math.round(sum / (participant)):0;
        List<Float> selectRate = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            selectRate.add(participant!=0?((float) Math.round(Collections.frequency(valueList, i) / participant * 100f)):0);
        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .questionAvg(avg)
                .satisfactionList(selectRate)
                .build();
    }

    //todo 해당 순위로의 선택률(?)
    private List<Float> selectAvg(List<Integer> selectCountList) {
        List<Float> rankList = new ArrayList<>();
        int totalSelectCount = selectCountList.stream().mapToInt(Integer::intValue).sum();
        for (Integer selectCount : selectCountList) {
            float selectAvg = (float) selectCount / totalSelectCount * 100f;
            rankList.add(selectAvg);
        }
        return rankList;
    }

    /*
        String 타입으로 저장된 Json 형식 문자열을 JsonObject
        로 사용할 수 있게 만들어주는 로직
        JsonObject 는 HashMap 을 상속받는 클래스이다. Key, Value 로 값에 접근할 수 있다.
     */
    private JSONObject toJsonObjectType(String selectValueToStringTypeJsonForm) {
        JSONObject jsonObject;
        try {
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(selectValueToStringTypeJsonForm);
        } catch (ParseException e) {
            /*
              에러 메세지는 로그를 통해 남기는 방식을 채택 -> 로그 남기는 방법 알아봐야함.
              에러가 터져서 멈추는게 아닌, 에러가 터진 데이터를 제외한 통계를 반환시켜주어야함.
              new CustomException(ErrorCode.INVALID_FORM_DATA);
             */
            jsonObject = null;
        }
        return jsonObject;
    }
}
