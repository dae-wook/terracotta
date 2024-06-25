package com.daesoo.terracotta.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.buildprogress.dto.BuildProgressInScheamticPostResponseDto;
import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.common.entity.SchematicPost;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SchematicPostListResponseDto extends AbstractSchematicPostResponseDto{


    @Builder
    public SchematicPostListResponseDto(SchematicPost schematicPost) {
        super(schematicPost);
    }
	

	public static SchematicPostListResponseDto of(SchematicPost schematicPost) {
		return SchematicPostListResponseDto.builder()
				.schematicPost(schematicPost)
				.build();
	}

	
	public static SchematicPostListResponseDto of(SchematicPost schematicPost, BuildProgress buildProgress) {
		return SchematicPostListResponseDto.builder()
				.schematicPost(schematicPost)
				.build();
	}
	
	
}
