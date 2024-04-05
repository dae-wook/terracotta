package com.daesoo.terracotta.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.common.entity.SchematicPost;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchematicPostListResponseDto {

	private Long id;
	
	private String title;
	
	private String description;
	
	private List<PostTagResponseDto> tags;
	
	private String memberName;
	
	private String image;
	
	private int buyCount;
	
	private int commentCount;
	
	private int price;
	
	private float star;
	
	private int size;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime modifiedAt;
	
	
	public static SchematicPostListResponseDto of(SchematicPost schematicPost) {
		return SchematicPostListResponseDto.builder()
				.id(schematicPost.getId())
				.title(schematicPost.getTitle())
				.description(schematicPost.getContent())
				.buyCount(schematicPost.getBuyCount())
				.commentCount(schematicPost.getCommentCount())
				.price(schematicPost.getPrice())
				.star(schematicPost.getStar())
				.size(schematicPost.getSize())
				.tags(schematicPost.getPostTags().stream().map(PostTagResponseDto::of).toList())
				.memberName(schematicPost.getMember().getMemberName())
				.image(schematicPost.getImage())
				.createdAt(schematicPost.getCreatedAt())
				.modifiedAt(schematicPost.getModifiedAt())
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
