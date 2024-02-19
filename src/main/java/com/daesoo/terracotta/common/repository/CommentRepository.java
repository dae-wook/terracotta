package com.daesoo.terracotta.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByMemberId(Long id);

}