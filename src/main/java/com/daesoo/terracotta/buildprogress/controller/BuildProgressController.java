package com.daesoo.terracotta.buildprogress.controller;

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

import com.daesoo.terracotta.buildprogress.dto.BuildProgressInScheamticPostResponseDto;
import com.daesoo.terracotta.buildprogress.dto.BuildProgressRequestDto;
import com.daesoo.terracotta.buildprogress.dto.BuildProgressResponseDto;
import com.daesoo.terracotta.buildprogress.service.BuildProgressService;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.exception.UnauthorizedException;
import com.daesoo.terracotta.member.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/builds")
@CrossOrigin(origins = {"http://localhost:3737", "https://terracotta-nu.vercel.app"})
public class BuildProgressController {

	private final BuildProgressService buildProgressService;
	
	@PostMapping("{schematicPostId}")
	public ResponseDto<BuildProgressResponseDto> createBuildProgress(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("schematicPostId") Long schematicPostId) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.CREATED, buildProgressService.createBuildProgress(userDetails.getUser(), schematicPostId));
	}
	
	@GetMapping
	public ResponseDto<Page<BuildProgressResponseDto>> getBuildProgressListByLoginMember(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, buildProgressService.getBuildProgressListByLoginMember(userDetails.getUser(), page, size));
	}
	
	@PutMapping("/{buildProgressId}")
	public ResponseDto<BuildProgressInScheamticPostResponseDto> updateBuildProgress(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@PathVariable("buildProgressId") Long buildProgressId,
			@RequestBody BuildProgressRequestDto dto) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, buildProgressService.updateBuildProgress(userDetails.getUser(), buildProgressId, dto));
	}
	
	@DeleteMapping("/{buildProgressId}")
	public ResponseDto<Boolean> deleteBuildProgress(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@PathVariable("buildProgressId") Long buildProgressId) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
		return ResponseDto.success(HttpStatus.OK, buildProgressService.deleteBuildProgress(userDetails.getUser(), buildProgressId));
	}
	
}
