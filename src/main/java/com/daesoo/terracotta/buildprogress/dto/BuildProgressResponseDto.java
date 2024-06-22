package com.daesoo.terracotta.buildprogress.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuildProgressResponseDto {
	
	private Long id;
	
	private String setting;
	
	private SchematicPostResponseDto schmaticPost;
	
	private LocalDateTime createdAt;
	
	
	
	
	public static BuildProgressResponseDto of(BuildProgress buildProgress) {
		return BuildProgressResponseDto.builder()
				.id(buildProgress.getId())
				.setting(buildProgress.getSetting())
				.schmaticPost(SchematicPostResponseDto.of(buildProgress.getSchematicPost()))
				.createdAt(buildProgress.getCreatedAt())
				.build();
				
	}
}
