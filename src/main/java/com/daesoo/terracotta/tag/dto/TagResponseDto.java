package com.daesoo.terracotta.tag.dto;

import com.daesoo.terracotta.common.entity.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TagResponseDto {
	private Long id;

    private String name;
    
    public static TagResponseDto of(Tag tag) {
    	return TagResponseDto.builder()
    			.id(tag.getTagId())
    			.name(tag.getName())
    			.build();
    }
}
