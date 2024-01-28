package com.daesoo.terracotta.tag.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.tag.dto.TagGroupResponseDto;
import com.daesoo.terracotta.tag.service.TagGroupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/tag-group")
@RequiredArgsConstructor
@Slf4j
public class TagGroupController {
	
	private final TagGroupService tagGroupService;

	@GetMapping
	public ResponseDto<List<TagGroupResponseDto>> getTagGroup() {
		return ResponseDto.success(HttpStatus.OK, tagGroupService.getTagGroup());
	}
	
}
