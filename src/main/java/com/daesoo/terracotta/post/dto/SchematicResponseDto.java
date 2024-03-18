package com.daesoo.terracotta.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.common.entity.PostTag;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.schematic.util.Schematic;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchematicResponseDto {

	private Schematic schematic;
	
	public static SchematicResponseDto of(Schematic schematic) {
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
