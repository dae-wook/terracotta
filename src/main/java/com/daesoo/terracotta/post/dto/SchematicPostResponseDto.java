package com.daesoo.terracotta.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.schematic.util.Schematic;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchematicPostResponseDto {

	private Long id;
	
	private String title;
	
	private String description;
	
	private List<PostTagResponseDto> tags;
	
	private String memberName;
	
	private String image;
	
	private Schematic schematic;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime modifiedAt;
	
	public static SchematicPostResponseDto of(SchematicPost schematicPost, Schematic schematic) {
		return SchematicPostResponseDto.builder()
				.id(schematicPost.getId())
				.title(schematicPost.getTitle())
				.description(schematicPost.getContent())
				.tags(schematicPost.getPostTags().stream().map(PostTagResponseDto::of).toList())
				.memberName(schematicPost.getMember().getMemberName())
				.schematic(schematic)
				.image(schematicPost.getImage())
				.createdAt(schematicPost.getCreatedAt())
				.modifiedAt(schematicPost.getModifiedAt())
				.build();
	}
	
	public static SchematicPostResponseDto of(SchematicPost schematicPost) {
		return SchematicPostResponseDto.builder()
				.id(schematicPost.getId())
				.title(schematicPost.getTitle())
				.description(schematicPost.getContent())
				.tags(schematicPost.getPostTags().stream().map(PostTagResponseDto::of).toList())
				.memberName(schematicPost.getMember().getMemberName())
				.schematic(null)
				.image(schematicPost.getImage())
				.createdAt(schematicPost.getCreatedAt())
				.modifiedAt(schematicPost.getModifiedAt())
				.build();
	}
	
}
