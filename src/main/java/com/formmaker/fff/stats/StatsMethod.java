package com.formmaker.fff.stats;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.stats.dto.DescriptiveResponse;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.SelectResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatsMethod {

    static public StatsMethod statsMethod = new StatsMethod();

    public QuestionStats statsChoice(List<Reply> replyList, Question question) {
        List<Answer> answerList = question.getAnswerList();
        List<Integer> countList = new ArrayList<>(Collections.nCopies(answerList.size(), 0));
        List<Integer> changeSelectValueList;
        int totalSelect = 0;
        for(Reply reply : replyList){
            changeSelectValueList = Stream
                    .of(reply.getSelectValue().split("\\|"))
                    .map(s -> Integer.parseInt(s)-1)
                    .toList();

            for (Integer selectValues : changeSelectValueList) {
                countList.set(selectValues, countList.get(selectValues)+1);
                totalSelect++;
            }
        }

        List<Float> averageList = getAverageList(countList, totalSelect);
        List<SelectResponse> selectResponseList = new ArrayList<>();

        for(int i = 0; i < answerList.size();i++){
            selectResponseList.add(new SelectResponse(answerList.get(i).getAnswerValue(), averageList.get(i)));
        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .selectList(selectResponseList)

                .build();
    }

    public QuestionStats statsSlide(List<Reply> replyList, Question question) {

        int volume = question.getVolume();

        List<Integer> countList = new ArrayList<>(Collections.nCopies(volume*2+1, 0));

        int totalSelect = replyList.size();
        int index;
        for (Reply userReply : replyList) {
            index = Integer.parseInt(userReply.getSelectValue()) + volume;
            countList.set(index, countList.get(index)+1);
        }
        List<Float> averageList = countList.stream()
                .map(value -> (Math.round(((float)value / totalSelect) * 1000) / 10.0f))
                .collect(Collectors.toList());

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .volume(question.getVolume())
                .satisfactionList(averageList)
                .build();
    }

    public QuestionStats statsRank(List<Reply> replyList, Question question) {

        List<Answer> answerList = question.getAnswerList();
        List<List<Integer>> answerValueList = new ArrayList<>();
        for(int i = 0; i<answerList.size(); i++){
            answerValueList.add(new ArrayList<>());
        }
        Map<Integer, Integer> rankMap;
        for(Reply reply : replyList){
            rankMap = new Gson().fromJson(reply.getSelectValue(), new TypeToken<Map<Integer,Integer>>(){}.getType());

            for(Integer key : rankMap.keySet()){
                answerValueList.get(key-1).add(rankMap.get(key));

            }
        }

        List<SelectResponse> selectResponseList = new ArrayList<>();
        List<Float> rankList = new ArrayList<>();
        int answerNum = 0;
        for (List<Integer> valuesOfAnswer : answerValueList) {
            List<Integer> selectCountList = new ArrayList<>();
            for (int i = 1; i <= answerValueList.size(); i++) {
                selectCountList.add(Collections.frequency(valuesOfAnswer, i));
            }
            rankList = getAverageList(selectCountList, replyList.size());

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
            selectRate.add(participant!=0?(Math.round((float)Collections.frequency(valueList, i) / participant * 1000f)/10f):0);
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
    private List<Float> getAverageList(List<Integer> selectValue, Integer total){
        return selectValue.stream()
                .map(value -> (Math.round(((float)value / total) * 1000) / 10.0f))
                .collect(Collectors.toList());
    }

    /*
        String 타입으로 저장된 Json 형식 문자열을 JsonObject
        로 사용할 수 있게 만들어주는 로직
        JsonObject 는 HashMap 을 상속받는 클래스이다. Key, Value 로 값에 접근할 수 있다.
     */
//    private JSONObject toJsonObjectType(String selectValueToStringTypeJsonForm) {
//        JSONObject jsonObject;
//        try {
//            JSONParser jsonParser = new JSONParser();
//            jsonObject = (JSONObject) jsonParser.parse(selectValueToStringTypeJsonForm);
//        } catch (ParseException e) {
//            /*
//              에러 메세지는 로그를 통해 남기는 방식을 채택 -> 로그 남기는 방법 알아봐야함.
//              에러가 터져서 멈추는게 아닌, 에러가 터진 데이터를 제외한 통계를 반환시켜주어야함.
//              new CustomException(ErrorCode.INVALID_FORM_DATA);
//             */
//            jsonObject = null;
//        }
//        return jsonObject;
//    }


}
