package com.formmaker.fff.reply.service;

import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.reply.dto.request.EachReply;
import com.formmaker.fff.reply.entity.Reply;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class ReplyMethod {

    // 랭크 답변 처리 로직
    public Reply replyRank(EachReply eachReply, UserDetailsImpl userDetails) {
        JSONObject jsonObject = new JSONObject();

        int index = 1;

        /**
         * 프론트에서 넣어온 형태 [1, 2, 3, 4, 5]
         * 각 요소는 AnswerNum 를 나타낸다..
         * 배열의 index 값이 순위로 여겨진다.
         */
        for (Integer value : eachReply.getSelectValue()) {
            jsonObject.put(value, index++);
        }

        String selectValueJsonForm = jsonObject.toJSONString();

        return new Reply(eachReply.getQuestionId(), eachReply.getQuestionNum(), eachReply.getQuestionType(), selectValueJsonForm, eachReply.getDescriptive(), userDetails.getUser());
    }


    // 별점, 스코어, 슬라이드, 단수응답 답변 처리 로직
    public Reply replySingleValue(EachReply eachReply, UserDetailsImpl userDetails) {
        String selectValue = eachReply.getSelectValue().get(0).toString();
        return new Reply(eachReply.getQuestionId(), eachReply.getQuestionNum(), eachReply.getQuestionType(), selectValue, eachReply.getDescriptive(), userDetails.getUser());
    }


    // 복수응답 답변 처리 로직
    public Reply replyMultipleChoice(EachReply eachReply, UserDetailsImpl userDetails) {
        List<Integer> selectValue = eachReply.getSelectValue();
        List<String> strSelectValue = selectValue.stream().map(String::valueOf).toList();
        StringJoiner valueJoiner = new StringJoiner("|");
        for (String str : strSelectValue) {
            valueJoiner.add(str);
        }
        return new Reply(eachReply.getQuestionId(), eachReply.getQuestionNum(), eachReply.getQuestionType(), valueJoiner.toString(), eachReply.getDescriptive(), userDetails.getUser());
    }


    // 단답형, 서술형 답변 처리 로직
    public Reply replyDescriptive(EachReply eachReply, UserDetailsImpl userDetails) {
        return new Reply(eachReply.getQuestionId(), eachReply.getQuestionNum(), eachReply.getQuestionType(), eachReply.getDescriptive(), eachReply.getDescriptive(), userDetails.getUser());
    }
}
