package com.daesoo.terracotta.comment.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.comment.dto.CommentRequestDto;
import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.comment.dto.MyCommentResponseDto;
import com.daesoo.terracotta.comment.dto.ReplyRequestDto;
import com.daesoo.terracotta.comment.dto.ReplyResponseDto;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.NotificationType;
import com.daesoo.terracotta.common.entity.Comment;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.Reply;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.repository.CommentRepository;
import com.daesoo.terracotta.common.repository.ReplyRepository;
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
	private final ReplyRepository replyRepository;
	private final SchematicPostRepository schematicPostRepository;
	private final NotificationService notificationService;
	


	public Page<CommentResponseDto> getCommentList(Long schematicPostId, Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		
		return commentRepository.findAllBySchematicPostId(schematicPostId, pageable).map(CommentResponseDto::of);
	}
	
	public Page<MyCommentResponseDto> getCommentListByLoginMember(Member member, Integer page, Integer size) {
		
//		commentRepository.findByMemberMemberId(member.getId());
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		return commentRepository.findByMemberId(pageable, member.getId()).map(MyCommentResponseDto::of);
	}
	
	public CommentResponseDto getComment(Long commentId) {
		// TODO Auto-generated method stub
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
				);
		
		
		return CommentResponseDto.of(comment);
	}

	@Transactional
	public CommentResponseDto createComment(CommentRequestDto dto, Member member) {
		
		
		SchematicPost schematicPost = schematicPostRepository.findById(dto.getSchematicPostId()).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage())
				);
		
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
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto dto, Member user) {
		
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
				);
		
		if(comment.getMember().getId() != user.getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		comment.update(dto);
		
		
		
		return CommentResponseDto.of(comment);
	}

	@Transactional
	public ReplyResponseDto createReply(ReplyRequestDto dto, Member member) {

		Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow( 
				() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
				);
		
		Optional<Reply> optionalReply = dto.getReplyId() != null ? replyRepository.findById(dto.getReplyId()) : null;
		String taggedMember = null;
		if(optionalReply != null && optionalReply.isPresent()) {
			Reply tagTargetReply = optionalReply.get();
			if(tagTargetReply.getComment().getId() != comment.getId()) {
				throw new IllegalArgumentException(ErrorMessage.REPLY_MISSMATCH.getMessage());
			}
			taggedMember = tagTargetReply.getMember().getMemberName();
		}
		
		SchematicPost schematicPost = comment.getSchematicPost();
		
		comment.getSchematicPost().addComment(comment);
		
		commentRepository.save(comment);
		
		Reply reply = Reply.create(dto, comment, member, taggedMember);
		
		
		replyRepository.save(reply);
		
		//알림 받을 Member 세팅
		Set<Member> notificationTargetMember = new HashSet<>();
		if(comment.getReplies().size() > 0) {
			for(Reply targetMemberReply : comment.getReplies()) {
				notificationTargetMember.add(targetMemberReply.getMember());
			}
			
		}
		
		notificationTargetMember.add(comment.getMember());
		notificationTargetMember.add(schematicPost.getMember());
		
		notificationService.createReplyNotification(NotificationRequestDto.create(dto.getContent(), NotificationType.COMMENT), schematicPost.getMember(), comment.getMember(), member, notificationTargetMember);
		
		return ReplyResponseDto.of(reply);
	}

	@Transactional
	public ReplyResponseDto updateReply(Long replyId, ReplyRequestDto dto, Member user) {
		Reply reply = replyRepository.findById(replyId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
				);
		
		if(reply.getMember().getId() != user.getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		
		reply.update(dto);
		
		return ReplyResponseDto.of(reply);
	}

	public Boolean deleteReply(Long replyId, Member user) {
		Reply reply = replyRepository.findById(replyId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
				);
		
		if(reply.getMember().getId() != user.getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		SchematicPost schematicPost = reply.getComment().getSchematicPost();
		schematicPost.decreaseCommentCount();
		
		replyRepository.delete(reply);
		
		return true;
	}

}
