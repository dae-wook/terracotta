package com.daesoo.terracotta.buildprogress.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AbstractBuildProgressResponseDto {
	
	private Long id;
	
	private String setting;
	
	private LocalDateTime createdAt;
	
    protected AbstractBuildProgressResponseDto(Long id, String setting, LocalDateTime createdAt) {
        this.id = id;
        this.setting = setting;
        this.createdAt = createdAt;
    }
	
}
