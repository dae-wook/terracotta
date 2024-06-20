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
	
	private String cameraPosition;
	
	private String cameraTarget;
	
	private String hiddenMaterials;
	
	private String visibleRangestart;
	
	private String visibleRangeEnd;
	
	private LocalDateTime createdAt;
	
	
	
	
	public static BuildProgressInScheamticPostResponseDto of(BuildProgress buildProgress) {
		return BuildProgressInScheamticPostResponseDto.builder()
				.id(buildProgress.getId())
				.cameraPosition(buildProgress.getCameraPosition())
				.cameraTarget(buildProgress.getCameraTarget())
				.hiddenMaterials(buildProgress.getHiddenMateriacls())
				.visibleRangestart(buildProgress.getVisibleRangeStart())
				.visibleRangeEnd(buildProgress.getVisibleRangeEnd())
				.createdAt(buildProgress.getCreatedAt())
				.build();
				
	}
}
