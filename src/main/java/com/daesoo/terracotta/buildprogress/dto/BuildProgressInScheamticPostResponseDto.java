package com.daesoo.terracotta.buildprogress.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuildProgressInScheamticPostResponseDto {
	
	private Long id;
	
	private String setting;
	
	private LocalDateTime createdAt;
	
	
	
	
	public static BuildProgressInScheamticPostResponseDto of(BuildProgress buildProgress) {
		return BuildProgressInScheamticPostResponseDto.builder()
				.id(buildProgress.getId())
				.setting(buildProgress.getSetting())
				.createdAt(buildProgress.getCreatedAt())
				.build();
				
	}
}
