package com.daesoo.terracotta.buildprogress.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildProgressRequestDto {
	
	private String cameraPosition;
	
	private String cameraTarget;
	
	private String hiddenMaterials;
	
	private String visibleRangeStart;
	
	private String visibleRangeEnd;
	
	private LocalDateTime createdAt;
	
}
