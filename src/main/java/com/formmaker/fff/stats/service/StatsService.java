package com.formmaker.fff.stats.service;


import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.StatsResponse;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Stream;

import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_SURVEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public StatsResponse getStats(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Question> questionList = survey.getQuestionList();

        List<QuestionStats> questionStatsList = new ArrayList<>();
        QuestionStats questionStats;
        List<Reply> replyList;

        for (Question question : questionList) {
            replyList = question.getReplyList();
            questionStats = question.getQuestionType().getStatsFn().apply(replyList, question);

            questionStatsList.add(questionStats);
        }

        return StatsResponse.builder()
                .dailyParticipants(null)
                .totalParticipant(survey.getParticipant())
                .totalQuestion(questionList.size())
                .surveyTitle(survey.getTitle())
                .surveySummary(survey.getSummary())
                .createAt(survey.getCreatedAt().toLocalDate())
                .startedAt(survey.getStartedAt())
                .endedAt(survey.getEndedAt())
                .status(survey.getStatus().toString())
                .achievement(survey.getAchievement())
                .achievementRate(achievementRateCal(survey.getParticipant(), survey.getAchievement()))
                .questionStatsList(questionStatsList).build();
    }

    private float achievementRateCal(int participant, int achievement) {
        float achievementRate = (float) participant / achievement * 100;
        String floatFormat = String.format("%.1f", achievementRate);
        return Float.parseFloat(floatFormat);
    }

    @Transactional(readOnly = true)
    public byte[] getStatsCsvFile(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                ()-> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Question> questionList = survey.getQuestionList();

        byte[] csvFile;

        CSVPrinter csvPrinter;
        StringWriter sw = new StringWriter();

        String[] headers = Stream.concat(Arrays.stream(new String[]{"유저아이디"}), Arrays.stream(questionList.stream().map(Question::getTitle).toArray(String[]::new))).toArray(String[]::new);
        Map<User, List<Reply>> userReply = new HashMap<>();
        for(Question question : questionList){
            for(Reply reply : question.getReplyList()){
                List<Reply> replyList = userReply.get(reply.getUser());
                if(replyList == null){
                    userReply.put(reply.getUser(), new ArrayList<>(List.of(reply)));
                }else{
                    userReply.get(reply.getUser()).add(reply);
                }

            }
        }

        try{
            csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT.withHeader(headers));
            int checkValue;
            for(User user : userReply.keySet()){
                List<Reply> replyList = userReply.get(user).stream().sorted(Comparator.comparing(Reply::getQuestionNum)).toList();
                List<String> userData = new ArrayList<>();
                String loginId = user.getLoginId();
                userData.add(loginId.substring(0, loginId.length()-3)+"***");
                checkValue = 1;
                for(Reply reply : replyList){
                    System.out.println(checkValue + " " + reply.getQuestionNum() + " " + reply.getSelectValue());
                    while (reply.getQuestionNum() > checkValue++){
                        userData.add("");
                    }
                    userData.add(replyToValue(reply));
                }
                csvPrinter.printRecord(userData);
            }
            sw.flush();
            csvFile = sw.toString().getBytes("MS949");
        }catch (IOException e){
            csvFile = null;
        }

        return csvFile;
    }

    public String replyToValue(Reply reply){
        QuestionTypeEnum type = reply.getQuestionType();
        switch (type){
            case MULTIPLE_CHOICE -> {return reply.getSelectValue().replace('|', ',');}
            case SHORT_DESCRIPTIVE, LONG_DESCRIPTIVE -> {return reply.getDescriptive();}
            case RANK -> {return reply.getSelectValue().substring(1, reply.getSelectValue().length()-1).replace("\"", "");}
        }
        return reply.getSelectValue();


    }
}
