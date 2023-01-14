package com.formmaker.fff.reply.dto.request;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.user.dto.UserDto;
import lombok.Getter;

@Getter
public class ReplyDto {
    private Long id;
    private Long questionId;
    private Integer questionNum;
    private QuestionTypeEnum questionType;
    private String selectValue;
    private String descriptive;
    private String rank;
    private Boolean status;
    private UserDto userDto;

    public ReplyDto(Reply reply) {
        this.id = reply.getId();
        this.questionId = reply.getQuestionId();
        this.questionNum = reply.getQuestionNum();
        this.questionType = reply.getQuestionType();
        this.selectValue = reply.getSelectValue();
        this.descriptive = reply.getDescriptive();
        this.rank = reply.getRank();
        this.status = reply.getStatus();
        this.userDto = new UserDto(reply.getUser());
    }
}
