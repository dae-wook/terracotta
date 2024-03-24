package com.daesoo.terracotta.post.dto;

import com.daesoo.terracotta.schematic.util.SchematicDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchematicResponseDto {

	private SchematicDto schematic;
	
	public static SchematicResponseDto of(SchematicDto schematic) {
		return SchematicResponseDto.builder()
				.schematic(schematic)
				.build();
	}
	
//	public static SchematicPostResponseDto of(PostTag postTag) {
//		return SchematicPostResponseDto.builder()
//				.id(postTag.getSchematicPost().getId())
//				.title(postTag.getSchematicPost().getTitle())
//				.description(postTag.getSchematicPost().getContent())
//				.buyCount(postTag.getSchematicPost().getBuyCount())
//				.commentCount(postTag.getSchematicPost().getCommentCount())
//				.price(postTag.getSchematicPost().getPrice())
//				.star(postTag.getSchematicPost().getStar())
//				.tags(postTag.getSchematicPost().getPostTags().stream().map(PostTagResponseDto::of).toList())
//				.memberName(postTag.getSchematicPost().getMember().getMemberName())
//				.schematic(null)
//				.image(postTag.getSchematicPost().getImage())
//				.createdAt(postTag.getSchematicPost().getCreatedAt())
//				.modifiedAt(postTag.getSchematicPost().getModifiedAt())
//				.build();
//	}
	
}
