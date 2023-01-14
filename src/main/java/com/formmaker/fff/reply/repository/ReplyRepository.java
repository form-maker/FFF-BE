package com.formmaker.fff.reply.repository;

import com.formmaker.fff.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByQuestionId(Long questionId);
}
