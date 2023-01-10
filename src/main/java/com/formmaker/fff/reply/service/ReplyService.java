package com.formmaker.fff.reply.service;

import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.reply.request.ReplyCreateRequest;
import com.formmaker.fff.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    public void postReply(Long surveyId, ReplyCreateRequest replyRequest, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        replyRepository.save(new Reply(replyRequest.getQuestionType(), replyRequest.getChoice(), replyRequest.getSelectValue(), replyRequest.getDescriptive(), replyRequest.getRank(), replyRequest.getQuestionId(), replyRequest.getQuestionNum(), user));
    }
}
