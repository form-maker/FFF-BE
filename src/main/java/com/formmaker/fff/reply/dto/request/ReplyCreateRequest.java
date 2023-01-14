package com.formmaker.fff.reply.dto.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReplyCreateRequest {
    private List<EachReply> replyList = new ArrayList<>();
}
