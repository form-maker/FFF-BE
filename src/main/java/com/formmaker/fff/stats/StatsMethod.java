package com.formmaker.fff.stats;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.stats.dto.DescriptiveList;
import com.formmaker.fff.stats.dto.DescriptiveResponse;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.SelectResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        List<Float> averageList = getAverageList(countList, totalSelect);

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
        List<Float> rankList;
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
        for(SelectResponse selectResponse : selectResponseList){
        }
        selectResponseList.sort((r1, r2) -> calculationToRank(r2.getRankList()).compareTo(calculationToRank(r1.getRankList())));

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .selectList(selectResponseList)
                .build();
    }

    private Float calculationToRank(List<Float> rank){
        float sum = 0.0f;
        int size = rank.size()-1;
        for(int i = 0; i < rank.size(); i++){
            sum += rank.get(i) * (size-i);
        }
        return sum;
    }

    public QuestionStats statsLongDescriptive(List<Reply> replyList, Question question) {
        List<DescriptiveResponse> descriptiveList = new ArrayList<>();
        int randomValue;

        List<String> descriptiveDataList = new ArrayList<>();

        for (Reply reply : replyList) {
            descriptiveDataList.add(reply.getDescriptive());
        }

        for(int i = 0; i < 3; i++){
            if(replyList.isEmpty()){
                break;
            }
            randomValue = (int)(Math.random() * 100)%replyList.size();
            descriptiveList.add(new DescriptiveResponse(replyList.remove(randomValue).getDescriptive()));
        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .descriptiveList(new DescriptiveList(descriptiveList, descriptiveDataList))

                .build();
    }


    public QuestionStats statsShortDescriptive(List<Reply> replyList, Question question) {

        ArrayList<DescriptiveResponse> descriptiveList = new ArrayList<>();

        List<String> shortDescriptiveList = replyList.stream().map(Reply::getDescriptive).toList();

        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String str : shortDescriptiveList) {
            map.merge(str, 1, Integer::sum);
        }
        for (String key : map.keySet()) {
            descriptiveList.add(new DescriptiveResponse(key, map.get(key)));
        }
        descriptiveList.sort((s1, s2) -> s2.getValue().compareTo(s1.getValue()));

        List<String> descriptiveDataList = new ArrayList<>();

        for (Reply reply : replyList) {
            descriptiveDataList.add(reply.getDescriptive());
        }

        return QuestionStats.builder()
                .questionNum(question.getQuestionNum())
                .questionType(question.getQuestionType())
                .questionTitle(question.getTitle())
                .questionSummary(question.getSummary())
                .descriptiveList(new DescriptiveList(descriptiveList,descriptiveDataList))

                .build();
    }

    public QuestionStats statsOfPositiveValue(List<Reply> replyList, Question question) {

        List<Integer> valueList = new ArrayList<>();

        for (Reply reply : replyList) {
            Integer value = Integer.parseInt(reply.getSelectValue());
            valueList.add(value);
        }


        // 평균을 구하는 로직
        int participant = valueList.size();
        int sum = valueList.stream().mapToInt(Integer::intValue).sum();
//        float avg = participant!=0?Math.round(sum / (participant)):0;
        float avg = participant!=0?(Math.round((float)sum / participant*10f)/10f):0;
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


    private List<Float> getAverageList(List<Integer> selectValue, Integer total){
        return selectValue.stream()
                .map(value -> (Math.round(((float)value / total) * 1000) / 10.0f))
                .collect(Collectors.toList());
    }


}
