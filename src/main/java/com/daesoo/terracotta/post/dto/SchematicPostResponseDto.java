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
	
	private String content;
	
	private List<PostTagResponseDto> tags;
	
	private String memberUserId;
	
	private String memberUsername;
	
	private String filePath;
	
	private Schematic schematic;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime modifiedAt;
	
	public static SchematicPostResponseDto of(SchematicPost schematicPost, Schematic schematic) {
		return SchematicPostResponseDto.builder()
				.id(schematicPost.getId())
				.title(schematicPost.getTitle())
				.content(schematicPost.getContent())
				.tags(schematicPost.getPostTags().stream().map(PostTagResponseDto::of).toList())
				.memberUserId(schematicPost.getMember().getUserId())
				.memberUsername(schematicPost.getMember().getUsername())
				.filePath(schematicPost.getFilePath())
				.schematic(schematic)
				.createdAt(schematicPost.getCreatedAt())
				.modifiedAt(schematicPost.getModifiedAt())
				.build();
	}
	
	public static SchematicPostResponseDto of(SchematicPost schematicPost) {
		return SchematicPostResponseDto.builder()
				.id(schematicPost.getId())
				.title(schematicPost.getTitle())
				.content(schematicPost.getContent())
				.tags(schematicPost.getPostTags().stream().map(PostTagResponseDto::of).toList())
				.memberUserId(schematicPost.getMember().getUserId())
				.memberUsername(schematicPost.getMember().getUsername())
				.filePath(schematicPost.getFilePath())
				.schematic(null)
				.createdAt(schematicPost.getCreatedAt())
				.modifiedAt(schematicPost.getModifiedAt())
				.build();
	}
	
}
