package com.formmaker.fff.reply.service;

import com.formmaker.fff.reply.dto.request.ReplyRequest;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.user.entity.User;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class ReplyMethod {

    // 랭크 답변 처리 로직
    public Reply replyToRank(ReplyRequest replyRequest, String loginId) {
        JSONObject jsonObject = new JSONObject();

        int rank = 1;
        for (Integer value : replyRequest.getSelectValue()) {
            jsonObject.put(value, rank++);
        }

        String selectValueJsonForm = jsonObject.toJSONString();

        return new Reply(replyRequest.getQuestionId(), replyRequest.getQuestionNum(), replyRequest.getQuestionType(), selectValueJsonForm, replyRequest.getDescriptive(), loginId);
    }

    public Reply replyToSingleValue(ReplyRequest replyRequest, String loginId) {
        String selectValue = replyRequest.getSelectValue().get(0).toString();
        return new Reply(replyRequest.getQuestionId(), replyRequest.getQuestionNum(), replyRequest.getQuestionType(), selectValue, null, loginId);
    }


    public Reply replyToMultipleValue(ReplyRequest replyRequest, String loginId) {
        List<Integer> selectValue = replyRequest.getSelectValue();
        List<String> strSelectValue = selectValue.stream().map(String::valueOf).toList();
        StringJoiner valueJoiner = new StringJoiner("|");
        for (String str : strSelectValue) {
            valueJoiner.add(str);
        }
        return new Reply(replyRequest.getQuestionId(), replyRequest.getQuestionNum(), replyRequest.getQuestionType(), valueJoiner.toString(), null, loginId);
    }


    public Reply replyToDescriptive(ReplyRequest replyRequest, String loginId) {
        return new Reply(replyRequest.getQuestionId(), replyRequest.getQuestionNum(), replyRequest.getQuestionType(), null, replyRequest.getDescriptive(), loginId);
    }
}
