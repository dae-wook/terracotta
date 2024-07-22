package com.daesoo.terracotta.comment;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.comment.dto.CommentRequestDto;
import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.comment.dto.ReplyRequestDto;
import com.daesoo.terracotta.comment.dto.ReplyResponseDto;
import com.daesoo.terracotta.comment.service.CommentService;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.exception.UnauthorizedException;
import com.daesoo.terracotta.member.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3737", "https://terracotta-nu.vercel.app"})
public class CommentController {
	
	private final CommentService commentService;
	
	@GetMapping
	public ResponseDto<Page<CommentResponseDto>> getCommentList(
			@RequestParam("schematicPostId") Long schematicPostId,
			@RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size) {
		return ResponseDto.success(HttpStatus.OK, commentService.getCommentList(schematicPostId, page, size));
	}
	
	@GetMapping("/{commentId}")
	public ResponseDto<CommentResponseDto> getComment(
			@PathVariable("commentId") Long commentId) {
		return ResponseDto.success(HttpStatus.OK, commentService.getComment(commentId));
	}

	@PostMapping
	public ResponseDto<CommentResponseDto> createComment(
			@RequestBody CommentRequestDto dto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, commentService.createComment(dto, userDetails.getUser()));
		
	}
	
	@DeleteMapping("/{commentId}")
	public ResponseDto<String> deleteComment(
			@PathVariable("commentId") Long commentId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, commentService.deleteComment(commentId, userDetails.getUser()));
	}
	
	@PutMapping("/{commentId}")
	public ResponseDto<CommentResponseDto> updateComment(
			@PathVariable("commentId") Long commentId,
			@RequestBody CommentRequestDto dto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, commentService.updateComment(commentId, dto, userDetails.getUser()));
	}
	
	@PostMapping("/replies")
	public ResponseDto<ReplyResponseDto> createReply(
			@RequestBody ReplyRequestDto dto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, commentService.createReply(dto, userDetails.getUser()));
		
	}
	
	@PutMapping("/replies/{replyId}")
	public ResponseDto<ReplyResponseDto> updateReply(
			@PathVariable("replyId") Long replyId,
			@RequestBody ReplyRequestDto dto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, commentService.updateReply(replyId, dto, userDetails.getUser()));
	}
	
	@DeleteMapping("/replies/{replyId}")
	public ResponseDto<Boolean> deleteReply(
			@PathVariable("replyId") Long replyId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, commentService.deleteReply(replyId, userDetails.getUser()));
	}
	
}