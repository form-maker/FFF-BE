package com.formmaker.fff.stats.service;


import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.participant.Participant;
import com.formmaker.fff.participant.ParticipantRepository;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.stats.dto.DailyParticipant;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.StatsResponse;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.user.entity.User;
import com.formmaker.fff.user.repository.UserRepository;
import com.formmaker.fff.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_SURVEY;
import static com.formmaker.fff.common.exception.ErrorCode.NOT_MATCH_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public StatsResponse getStats(Long surveyId, String start, String end, Long userId) {
        Survey survey = surveyRepository.findByIdAndStatusNot(surveyId, StatusTypeEnum.DELETE).orElseThrow(

                () -> new CustomException(NOT_FOUND_SURVEY)
        );

        if(!survey.getUserId().equals(userId)){
            throw new CustomException(NOT_MATCH_USER);
        }

        List<QuestionStats> questionStatsList = new ArrayList<>();
        List<Question> questionList = survey.getQuestionList();
        List<Reply> replyList;

        LocalDate startedAT;
        LocalDate endedAT;
        try{
            startedAT = LocalDate.parse(start);
            endedAT = LocalDate.parse(end);
        }catch (DateTimeParseException e){
            startedAT = LocalDate.of(1, 1, 1);
            endedAT = LocalDate.of(9999, 1, 1);
        }
        List<Participant> participantList = participantRepository.findAllBySurvey(survey);

        for (Question question : questionList) {
            replyList = replyRepository.findAllByQuestionIdAndCreatedAtAfterAndCreatedAtBefore(question.getId(), startedAT.atStartOfDay(), endedAT.atStartOfDay());

            questionStatsList.add(question.getQuestionType().getStatsFn().apply(replyList, question));
        }
        return StatsResponse.builder()
                .dailyParticipantList(getDailyParticipant(participantList, survey.getStartedAt(), survey.getEndedAt()))
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

    private DailyParticipant getDailyParticipant(List<Participant> replyList, LocalDate start, LocalDate end){
        Map<LocalDate, Integer> dailyCount = new HashMap<>();
        DailyParticipant dailyParticipant = new DailyParticipant();

        for(LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)){
            dailyCount.put(day, 0);
        }

        for(Participant participant : replyList){
            if(dailyCount.containsKey(participant.getCreatedAt().toLocalDate())){
                dailyCount.put(participant.getCreatedAt().toLocalDate(), dailyCount.get(participant.getCreatedAt().toLocalDate())+1);
            }


        }

        for(LocalDate day : dailyCount.keySet()){
            dailyParticipant.addDate(day);
            dailyParticipant.addParticipant(dailyCount.get(day));
        }
        Collections.reverse(dailyParticipant.getDate());
        Collections.reverse(dailyParticipant.getParticipant());


        return dailyParticipant;

    }

    private float achievementRateCal(int participant, int achievement) {
        float achievementRate = (float) participant / achievement * 100;
        String floatFormat = String.format("%.1f", achievementRate);
        return Float.parseFloat(floatFormat);
    }
    @Transactional(readOnly = true)

    public Pair<String, byte[]> getStatsCsvFile(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                ()-> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Question> questionList = survey.getQuestionList();

        byte[] csvFile;

        CSVPrinter csvPrinter;
        StringWriter sw = new StringWriter();

        String[] headers = Stream.concat(Arrays.stream(new String[]{"유저아이디", "유저이메일"}), Arrays.stream(questionList.stream().map(Question::getTitle).toArray(String[]::new))).toArray(String[]::new);
        try{
            csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT.withHeader(headers));
            int checkValue;
            for(Participant participant : survey.getParticipantList()){
                User user = userRepository.findByLoginId(participant.getLoginId()).orElse(new User(null, "비회원", "비회원"));

                List<Reply> replyList = participant.getReplyList().stream().sorted(Comparator.comparing(Reply::getQuestionNum)).toList();
                List<String> userData = new ArrayList<>();
                String loginId = user.getLoginId();
                userData.add(ShadeToLoginId(user));
                userData.add(user.getEmail());
                checkValue = 1;
                for(Reply reply : replyList){
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


        return Pair.of(survey.getTitle(), csvFile);
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

    @Transactional
    public XSSFWorkbook getStatsXlsxFile(Long surveyId){
        XSSFWorkbook wd = new XSSFWorkbook();
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                ()->new CustomException(NOT_FOUND_SURVEY)
        );
        XSSFSheet sheet = wd.createSheet(survey.getTitle());
        Row row = null;
        Cell cell = null;

        List<Question> questionList = survey.getQuestionList();

        row = sheet.createRow(0);
        row.createCell(0).setCellValue("유저 아이디");
        row.createCell(1).setCellValue("유저 이메일");

        int cellIndex = 2;
        for(Question question : questionList){
            row.createCell(cellIndex++).setCellValue(question.getTitle());
        }
        int rowIndex = 1;
        int checkValue;
        for(Participant participant : survey.getParticipantList()){
            cellIndex = 0;
            row = sheet.createRow(rowIndex);
            User user = userRepository.findByLoginId(participant.getLoginId()).orElse(new User(null, "비회원", "비회원"));
            row.createCell(cellIndex++).setCellValue(ShadeToLoginId(user));
            row.createCell(cellIndex++).setCellValue(user.getEmail());
            checkValue = 1;
            for(Reply reply : participant.getReplyList()){
                while (reply.getQuestionNum() > checkValue++){
                    row.createCell(cellIndex++).setCellValue("");
                }
                row.createCell(cellIndex++).setCellValue(replyToValue(reply));

            }
            rowIndex++;

        }
        return wd;
    }

    private String ShadeToLoginId(User user){
        String loginId = user.getLoginId();
        return loginId==null?"비회원":(loginId.substring(0, loginId.length()-3)+"***");
    }
}
