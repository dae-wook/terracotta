package com.daesoo.terracotta.schematic.util;

import com.daesoo.terracotta.buildprogress.dto.BuildProgressInScheamticPostResponseDto;
import com.daesoo.terracotta.common.entity.BuildProgress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchematicDto {
	
	private int width;
	private int height;
	private int length;
	private int size;
	private BuildProgressInScheamticPostResponseDto buildProgress;
	byte[] blockData;
	
	public void setBuildProgress(BuildProgress buildProgress) {
		this.buildProgress = BuildProgressInScheamticPostResponseDto.of(buildProgress);
	}
}
