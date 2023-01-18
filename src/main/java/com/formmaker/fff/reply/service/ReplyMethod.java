package com.formmaker.fff.reply.service;

import com.formmaker.fff.reply.dto.request.EachReplyRequest;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.user.entity.User;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class ReplyMethod {

    // 랭크 답변 처리 로직
    public Reply replyToRank(EachReplyRequest eachReplyRequest, User user) {
        JSONObject jsonObject = new JSONObject();

        int rank = 1;

        /**
         * 프론트에서 넣어온 형태 [5, 3, 2, 1, 4]
         * 각 요소는 AnswerNum 를 나타낸다..
         * 배열의 index 값이 순위로 여겨진다.
         */
        for (Integer value : eachReplyRequest.getSelectValue()) {
            jsonObject.put(value, rank++);
        }

        String selectValueJsonForm = jsonObject.toJSONString();

        return new Reply(eachReplyRequest.getQuestionId(), eachReplyRequest.getQuestionNum(), eachReplyRequest.getQuestionType(), selectValueJsonForm, eachReplyRequest.getDescriptive(), user);
    }

    // 별점, 스코어, 슬라이드, 단수응답 답변 처리 로직
    public Reply replyToSingleValue(EachReplyRequest eachReplyRequest, User user) {
        String selectValue = eachReplyRequest.getSelectValue().get(0).toString();
        return new Reply(eachReplyRequest.getQuestionId(), eachReplyRequest.getQuestionNum(), eachReplyRequest.getQuestionType(), selectValue, null, user);
    }


    // 복수응답 답변 처리 로직
    public Reply replyToMultipleValue(EachReplyRequest eachReplyRequest, User user) {
        List<Integer> selectValue = eachReplyRequest.getSelectValue();
        List<String> strSelectValue = selectValue.stream().map(String::valueOf).toList();
        StringJoiner valueJoiner = new StringJoiner("|");
        for (String str : strSelectValue) {
            valueJoiner.add(str);
        }
        return new Reply(eachReplyRequest.getQuestionId(), eachReplyRequest.getQuestionNum(), eachReplyRequest.getQuestionType(), valueJoiner.toString(), null, user);
    }


    // 단답형, 서술형 답변 처리 로직
    public Reply replyToDescriptive(EachReplyRequest eachReplyRequest, User user) {
        return new Reply(eachReplyRequest.getQuestionId(), eachReplyRequest.getQuestionNum(), eachReplyRequest.getQuestionType(), null, eachReplyRequest.getDescriptive(), user);
    }
}
