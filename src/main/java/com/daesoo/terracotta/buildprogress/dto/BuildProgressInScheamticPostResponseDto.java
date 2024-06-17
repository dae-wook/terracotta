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
	
	private String position;
	
	private String target;
	
	private String hiddenMateriacls;
	
	private String start;
	
	private String end;
	
	private LocalDateTime createdAt;
	
	
	
	
	public static BuildProgressInScheamticPostResponseDto of(BuildProgress buildProgress) {
		return BuildProgressInScheamticPostResponseDto.builder()
				.id(buildProgress.getId())
				.position(buildProgress.getPosition())
				.target(buildProgress.getTarget())
				.hiddenMateriacls(buildProgress.getHiddenMateriacls())
				.start(buildProgress.getStart())
				.end(buildProgress.getEnd())
				.createdAt(buildProgress.getCreatedAt())
				.build();
				
	}
}
