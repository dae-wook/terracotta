package com.daesoo.terracotta.post.dto;

import com.daesoo.terracotta.common.entity.PostTag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostTagResponseDto {
	
	private Long id;
	
	private String name;
	
	public static PostTagResponseDto of(PostTag postTag) {
		return PostTagResponseDto.builder()
				.id(postTag.getTag().getTagId())
				.name(postTag.getTag().getName())
				.build();
	}

}
