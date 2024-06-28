package com.daesoo.terracotta.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.buildprogress.dto.BuildProgressInScheamticPostResponseDto;
import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.common.entity.SchematicPost;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SchematicPostResponseDto extends AbstractSchematicPostResponseDto{


	
	private BuildProgressInScheamticPostResponseDto buildProgress;
	

	
    @Builder
    public SchematicPostResponseDto(SchematicPost schematicPost, BuildProgressInScheamticPostResponseDto buildProgress) {
        super(schematicPost);
        this.buildProgress = buildProgress;
    }
	

	public static SchematicPostResponseDto of(SchematicPost schematicPost) {
		return SchematicPostResponseDto.builder()
				.schematicPost(schematicPost)
				.buildProgress(null)
				.build();
	}

	
	public static SchematicPostResponseDto of(SchematicPost schematicPost, BuildProgress buildProgress) {
		return SchematicPostResponseDto.builder()
				.schematicPost(schematicPost)
				.buildProgress(buildProgress != null ? BuildProgressInScheamticPostResponseDto.of(buildProgress) : null)
				.build();
	}
	
	public static SchematicPostResponseDto of(BuildProgress buildProgress) {
		return SchematicPostResponseDto.builder()
				.schematicPost(buildProgress.getSchematicPost())
				.buildProgress(buildProgress != null ? BuildProgressInScheamticPostResponseDto.of(buildProgress) : null)
				.build();
	}
	
	
}
