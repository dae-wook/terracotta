package com.daesoo.terracotta.tag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.entity.TagGroup;
import com.daesoo.terracotta.common.repository.TagGroupRepository;
import com.daesoo.terracotta.tag.dto.TagGroupResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagGroupService {
	
	private final TagGroupRepository tagGroupRepository;

	public List<TagGroupResponseDto> getTagGroup() {
		
		List<TagGroup> tagCategories = tagGroupRepository.findAll();
		
		return tagCategories.stream().map(TagGroupResponseDto::of).toList();
	}
	

	
	
}
