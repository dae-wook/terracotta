package com.daesoo.terracotta.tag.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.daesoo.terracotta.common.entity.TagGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TagGroupResponseDto {
	
	private Long tagGroupId;
	
	private String groupName;
	
	private List<TagResponseDto> tags;
	
	public static TagGroupResponseDto of(TagGroup tagGroup) {
		return TagGroupResponseDto.builder()
				.tagGroupId(tagGroup.getId())
				.groupName(tagGroup.getName())
				.tags(tagGroup.getTags().stream().map(TagResponseDto::of).collect(Collectors.toList()))
				.build();
	}
	
}