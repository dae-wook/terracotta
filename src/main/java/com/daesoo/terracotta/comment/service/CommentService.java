package com.daesoo.terracotta.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.comment.dto.CommentRequestDto;
import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.NotificationType;
import com.daesoo.terracotta.common.entity.Comment;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.repository.CommentRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.notification.NotificationService;
import com.daesoo.terracotta.notification.dto.NotificationRequestDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final SchematicPostRepository schematicPostRepository;
	private final NotificationService notificationService;
	


	public Page<CommentResponseDto> getComment(Long schematicPostId, Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		
		return commentRepository.findAllBySchematicPostId(schematicPostId, pageable).map(CommentResponseDto::of);
	}

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
		
		//작성자에게 Notification 생성;
		notificationService.createNotification(NotificationRequestDto.create(dto.getContent(), NotificationType.COMMENT), schematicPost.getMember());
		
		return CommentResponseDto.of(comment);
	}

	@Transactional
	public String deleteComment(Long commentId, Member user) {
		
		
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
				);
		
		if(comment.getMember().getId() != user.getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		SchematicPost schematicPost = comment.getSchematicPost();
		schematicPost.deleteComment(comment);
		
		commentRepository.delete(comment);
		
		return "삭제 성공";
	}

	@Transactional
	public String updateComment(Long commentId, CommentRequestDto dto, Member user) {
		
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
				);
		
		if(comment.getMember().getId() != user.getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		float oldStar = comment.getStar();
		comment.update(dto);
		
		SchematicPost schematicPost = comment.getSchematicPost();
		schematicPost.updateComment(comment, oldStar);
		
		
		return "수정 성공";
	}

}
