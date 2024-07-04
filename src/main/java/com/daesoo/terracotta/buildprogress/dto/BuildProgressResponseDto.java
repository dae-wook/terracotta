package com.daesoo.terracotta.buildprogress.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.post.dto.SchematicPostListResponseDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BuildProgressResponseDto extends AbstractBuildProgressResponseDto{
	
	
//	private SchematicPostListResponseDto schematicPost;
	
    @Builder
    public BuildProgressResponseDto(Long id, String setting, SchematicPostListResponseDto schematicPost, LocalDateTime createdAt) {
        super(id, setting, createdAt);
//        this.schematicPost = schematicPost;
    }
	
	
	public static BuildProgressResponseDto of(BuildProgress buildProgress) {
		return BuildProgressResponseDto.builder()
				.id(buildProgress.getId())
				.setting(buildProgress.getSetting())
//				.schematicPost(SchematicPostListResponseDto.of(buildProgress.getSchematicPost()))
				.createdAt(buildProgress.getCreatedAt())
				.build();
				
	}
}
