package com.formmaker.fff.reply.repository;

import com.formmaker.fff.participant.Participant;
import com.formmaker.fff.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByQuestionIdAndCreatedAtAfterAndCreatedAtBefore(Long questionId, LocalDateTime start, LocalDateTime end);

    List<Reply> findAllByParticipant(Participant participant);
}
