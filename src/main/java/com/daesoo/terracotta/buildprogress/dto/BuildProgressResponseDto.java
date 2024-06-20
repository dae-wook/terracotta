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
	
	private String cameraPosition;
	
	private String cameraTarget;
	
	private String hiddenMaterials;
	
	private String visibleRangeStart;
	
	private String visibleRangeEnd;
	
	private SchematicPostResponseDto schmaticPost;
	
	private LocalDateTime createdAt;
	
	
	
	
	public static BuildProgressResponseDto of(BuildProgress buildProgress) {
		return BuildProgressResponseDto.builder()
				.id(buildProgress.getId())
				.cameraPosition(buildProgress.getCameraPosition())
				.cameraTarget(buildProgress.getCameraTarget())
				.hiddenMaterials(buildProgress.getHiddenMateriacls())
				.visibleRangeStart(buildProgress.getVisibleRangeStart())
				.visibleRangeEnd(buildProgress.getVisibleRangeEnd())
				.schmaticPost(SchematicPostResponseDto.of(buildProgress.getSchematicPost()))
				.createdAt(buildProgress.getCreatedAt())
				.build();
				
	}
}
