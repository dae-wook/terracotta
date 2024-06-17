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
	
	private String position;
	
	private String target;
	
	private String hiddenMateriacls;
	
	private String start;
	
	private String end;
	
	private SchematicPostResponseDto schmaticPost;
	
	private LocalDateTime createdAt;
	
	
	
	
	public static BuildProgressResponseDto of(BuildProgress buildProgress) {
		return BuildProgressResponseDto.builder()
				.id(buildProgress.getId())
				.position(buildProgress.getPosition())
				.target(buildProgress.getTarget())
				.hiddenMateriacls(buildProgress.getHiddenMateriacls())
				.start(buildProgress.getStart())
				.end(buildProgress.getEnd())
				.schmaticPost(SchematicPostResponseDto.of(buildProgress.getSchematicPost()))
				.createdAt(buildProgress.getCreatedAt())
				.build();
				
	}
}
