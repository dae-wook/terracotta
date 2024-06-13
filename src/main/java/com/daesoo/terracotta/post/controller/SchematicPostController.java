package com.daesoo.terracotta.post.controller;

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

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.exception.UnauthorizedException;
import com.daesoo.terracotta.member.UserDetailsImpl;
import com.daesoo.terracotta.post.dto.SchematicPostRequestDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;
import com.daesoo.terracotta.post.service.SchematicPostService;
import com.daesoo.terracotta.schematic.util.SchematicDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/schematic-posts")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3737", "https://terracotta-nu.vercel.app"})
@Slf4j
public class SchematicPostController {
	
	private final SchematicPostService schematicPostService;

	@PostMapping
	public ResponseDto<SchematicPostResponseDto> createSchematicPost(
			SchematicPostRequestDto schematicPostRequestDto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.createSchematicPost(schematicPostRequestDto, userDetails.getUser()));
	}
	
	@PutMapping("{schematicPostId}")
	public ResponseDto<SchematicPostResponseDto> updateSchematicPost(
			@PathVariable("schematicPostId") Long schematicPostId,
			SchematicPostRequestDto schematicPostRequestDto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		System.out.println(schematicPostRequestDto.getTags());
		System.out.println(schematicPostRequestDto.getTitle());
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.updateSchematicPost(schematicPostId, schematicPostRequestDto, userDetails.getUser()));
	}
	
	@GetMapping
	public ResponseDto<Page<SchematicPostResponseDto>> getSchematicPostList(
            @RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size,
            @RequestParam(name="tags", defaultValue = "0") Long[] tags
			) {
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematicPostList(page, size, tags));
	}
	
	@GetMapping("member/{nickname}")
	public ResponseDto<Page<SchematicPostResponseDto>> getSchematicPostListByMemberName(
			@PathVariable("nickname") String nickname,
            @RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size
			) {
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematicPostListByMemberName(nickname, page, size));
	}
	
	@GetMapping("{schematicPostId}")
	public ResponseDto<SchematicPostResponseDto> getSchematicPost(
			@PathVariable("schematicPostId") Long schematicPostId) {
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematicPost(schematicPostId));
	}
	
	@GetMapping("{schematicPostId}/schematic")
	public ResponseDto<SchematicDto> getSchematic(
			@PathVariable("schematicPostId") Long schematicPostId) {
		return ResponseDto.success(HttpStatus.OK, schematicPostService.getSchematic(schematicPostId));
	}
	
	@DeleteMapping("{schematicPostId}")
	public ResponseDto<Boolean> deleteSchematic(
			@PathVariable("schematicPostId") Long schematicPostId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		return ResponseDto.success(HttpStatus.OK, schematicPostService.deleteSchematic(schematicPostId, userDetails.getUser()));
	}
	
	
}