package com.daesoo.terracotta.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.entity.PostTag;
import com.daesoo.terracotta.common.exception.UnauthorizedException;
import com.daesoo.terracotta.member.UserDetailsImpl;
import com.daesoo.terracotta.post.dto.SchematicPostRequestDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;
import com.daesoo.terracotta.post.service.SchematicPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schematic-posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SchematicPostController {
	
	private final SchematicPostService schematicPostService;

	@PostMapping
	public ResponseDto<SchematicPostResponseDto> createSchematicPost(
			SchematicPostRequestDto schematicPostRequestDto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage()); // 또는 다른 예외 처리
	    }
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.createSchematicPost(schematicPostRequestDto, userDetails.getUser()));
	}
	
	@GetMapping
	public ResponseDto<Page<SchematicPostResponseDto>> getSchematicPostList(
            @RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size,
            @RequestParam(name="tags", defaultValue = "0") Long[] tags
			) {
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematicPostList(page, size, tags));
	}
	
	@GetMapping("{schematicPostId}")
	public ResponseDto<SchematicPostResponseDto> getSchematicPost(
			@PathVariable("schematicPostId") Long schematicPostId) {
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematicPost(schematicPostId));
	}
	
}