package com.formmaker.fff.stats;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.SelectResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatsMethod {

    static public StatsMethod statsMethod = new StatsMethod();

    public QuestionStats statsSingleChoice(List<Reply> replyList, Question question) {

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

            List<String> selectValueList = List.of(userReply.getSelectValue().split("|"));  //유저들의 문항선택 값을 List에 넣어주고
            List<Integer> changeSelectValueList = selectValueList.stream() //스트링 리스트를 Integer 리스트로 변환
                    .map(Integer::parseInt).toList();


            for (Integer selectValues : changeSelectValueList) { // 유저들의 문항선택 값 리스트 값들을 selectValues 하나하나 넣어주고
                selectList.get(selectValues - 1).increaseValue(); //번호별로 정렬된 문항에서 유저가 문항선택한 값 들이 나올 때마다 해당 문항의 벨류는 증가하고
                totalSelect++; // 해당 Question 전체 선택 횟수가 증가한다.
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

        return QuestionStats.builder().build();
    }

    public QuestionStats statsRank(List<Reply> replyList, Question question) {
        /*
          모든 랭크 타입의 응답을 List 로 저장
          단일 요소의 형태 {Key:Value, Key:Value, Key:Value ...}
         */
        List<JSONObject> rankObjectList = new ArrayList<>();
        for (Reply reply : replyList) {
            JSONObject rankObject = toJsonObjectType(reply.getSelectValue());
            rankObjectList.add(rankObject);
        }

        List<SelectResponse> selectResponseList = new ArrayList<>();
        List<List<Integer>> selectValueList = new ArrayList<>();

        for (JSONObject rankObject : rankObjectList) {
            List<Integer> selectValue = new ArrayList<>();
            for (int answerNum = 0; answerNum < rankObject.size(); answerNum++) {
//                selectValue.add((Integer) rankObject.get(String.valueOf(answerNum)));
                selectValue = new ArrayList<>(rankObject.values());
            }
            selectValueList.add(selectValue);
        }

        //todo(삭제)
        System.out.println("selectValueList=======================================" + selectValueList);

        List<List<Integer>> valuesOfAnswers = new ArrayList<>();

        for (int i = 0; i < selectValueList.get(0).size(); i++) {
            List<Integer> eachValuesOfAnswer = new ArrayList<>();
            for (int j = 0; j < selectValueList.size(); j++) {
                eachValuesOfAnswer.add(Integer.parseInt(""+selectValueList.get(j).get(i)));
            }
            valuesOfAnswers.add(eachValuesOfAnswer);
        }

        //todo(삭제)
        System.out.println("valuesOfAnswers=======================================" + valuesOfAnswers);

        List<Float> rankList = new ArrayList<>();

        for (List<Integer> valuesOfAnswer : valuesOfAnswers) {
            List<Integer> selectCountList = new ArrayList<>();
            for (int i = 0; i < valuesOfAnswers.size(); i++) {
                selectCountList.add(Collections.frequency(valuesOfAnswer, (i + 1)));
            }

            //todo
            System.out.println("selectCountList = " + selectCountList);
            rankList = selectAvg(selectCountList);

            //todo 문항 값 넣어주기
            List<Answer> answerList = question.getAnswerList();
            SelectResponse selectResponse = new SelectResponse();
            for (Answer answer : answerList) {
                selectResponse = new SelectResponse(answer.getAnswerValue(), rankList);
            }
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

    //todo 해당 순위로의 선택률(?)
    private List<Float> selectAvg(List<Integer> selectCountList) {
        List<Float> rankList = new ArrayList<>();

        int totalSelectCount = selectCountList.stream().mapToInt(Integer::intValue).sum();
        for (Integer selectCount : selectCountList) {
            float selectAvg = (float) selectCount / totalSelectCount;
            rankList.add(selectAvg);

            //todo selectAvg
            System.out.println("selectAvg = " + selectAvg);
        }

        System.out.println("rankList = " + rankList);
        return rankList;
    };

    /**
     * String 타입으로 저장된 Json 형식 문자열을 JsonObject
     * 로 사용할 수 있게 만들어주는 로직
     * JsonObject 는 HashMap 을 상속받는 클래스이다. Key, Value 로 값에 접근할 수 있다.
     */
    private JSONObject toJsonObjectType(String selectValueToStringTypeJsonForm) {
        JSONObject jsonObject;
        try {
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(selectValueToStringTypeJsonForm);
        } catch (ParseException e) {
            /**
             * 에러 메세지는 로그를 통해 남기는 방식을 채택 -> 로그 남기는 방법 알아봐야함.
             * 에러가 터져서 멈추는게 아닌, 에러가 터진 데이터를 제외한 통계를 반환시켜주어야함.
             * new CustomException(ErrorCode.INVALID_FORM_DATA);
             */
            jsonObject = null;
        }
        return jsonObject;
    }
}
