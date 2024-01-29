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

import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.member.UserDetailsImpl;
import com.daesoo.terracotta.post.dto.SchematicPostRequestDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;
import com.daesoo.terracotta.post.service.SchematicPostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/schematic-posts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SchematicPostController {
	
	private final SchematicPostService schematicPostService;

	@PostMapping
	public ResponseDto<SchematicPostResponseDto> createSchematicPost(
			SchematicPostRequestDto schematicPostRequestDto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseDto.success(HttpStatus.OK, schematicPostService.createSchematicPost(schematicPostRequestDto, userDetails.getUser()));
	}
	
	@GetMapping
	public ResponseDto<Page<SchematicPostResponseDto>> getSchematicPostList(
            @RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size
			) {
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematicPostList(page, size));
	}
	
	@GetMapping("{schematicPostId}")
	public ResponseDto<SchematicPostResponseDto> getSchematicPost(
			@PathVariable("schematicPostId") Long schematicPostId) {
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematicPost(schematicPostId));
	}
	
}