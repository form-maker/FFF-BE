package com.formmaker.fff.reply;

import com.formmaker.fff.common.security.UserDetailsImpl;
import com.formmaker.fff.survey.request.ReplyRequest;
import com.formmaker.fff.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    public void postReply(Long surveyId, ReplyRequest replyRequest, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        replyRepository.save(new Reply(replyRequest.getQuestionType(), replyRequest.getChoice(), replyRequest.getSelectValue(), replyRequest.getDescriptive(), replyRequest.getRank(), replyRequest.getQuestionId(), replyRequest.getQuestionNum(), user));
    }
}
