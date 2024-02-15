package com.daesoo.terracotta.comment.service;

import org.springframework.stereotype.Service;

import com.daesoo.terracotta.comment.dto.CommentRequestDto;
import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.Comment;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.repository.CommentRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final SchematicPostRepository schematicPostRepository;

	@Transactional
	public CommentResponseDto createComment(CommentRequestDto dto, Member member) {
		
		if(dto.getStar() < 1.0f || dto.getStar() > 5.0f) {
			throw new IllegalArgumentException(ErrorMessage.OUT_OF_STAR_RANGE.getMessage());
		}
		
		SchematicPost schematicPost = schematicPostRepository.findById(dto.getSchematicPostId()).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage())
				);
		
		//댓글을 이미 달았을때 Exception.. TODO: 성능 개선 여지 있음
		for(Comment comment : schematicPost.getComments()) {
			if(comment.getMember().getEmail().equals(member.getEmail())) {
				throw new IllegalArgumentException(ErrorMessage.ALREADY_EXIST_COMMENT.getMessage());
			}
		}
		
		Comment comment = Comment.create(dto, schematicPost, member);
		
		schematicPost.addComment(comment);
		commentRepository.save(comment);
		
		
		return CommentResponseDto.of(comment);
	}

}
