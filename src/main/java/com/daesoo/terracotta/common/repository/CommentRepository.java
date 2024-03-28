package com.daesoo.terracotta.common.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	Page<Comment> findByMemberId(Pageable pageable, Long id);

	Page<Comment> findAllBySchematicPostId(Long schematicPostId, Pageable pageable);

}
