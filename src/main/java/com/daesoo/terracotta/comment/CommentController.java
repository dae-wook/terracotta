package com.daesoo.terracotta.comment;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.comment.dto.CommentRequestDto;
import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.comment.service.CommentService;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.member.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {
	
	private final CommentService commentService;

	@PostMapping
	public ResponseDto<CommentResponseDto> createComment(
			CommentRequestDto dto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		return ResponseDto.success(HttpStatus.OK, commentService.createComment(dto, userDetails.getUser()));
		
	}
	
}