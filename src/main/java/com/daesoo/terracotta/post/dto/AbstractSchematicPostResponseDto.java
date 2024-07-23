package com.daesoo.terracotta.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.member.dto.MemberInfoResponseDto;

import lombok.Getter;

@Getter
public class AbstractSchematicPostResponseDto {

	private Long id;
	
	private String title;
	
	private String description;
	
	private List<PostTagResponseDto> tags;
	
	private MemberInfoResponseDto member;
	
	private List<ImageResponseDto> images;
	
	private int buyCount;
	
	private int commentCount;
	
	private int price;
	
	private float star;
	
	private int size;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime modifiedAt;
	
    protected AbstractSchematicPostResponseDto(SchematicPost schematicPost) {
        this.id = schematicPost.getId();
        this.title = schematicPost.getTitle();
        this.description = schematicPost.getContent();
        this.tags = schematicPost.getPostTags().stream().map(PostTagResponseDto::of).toList();
        this.member = MemberInfoResponseDto.of(schematicPost.getMember());
        this.images = schematicPost.getImages().stream().map(ImageResponseDto::of).toList();
        this.buyCount = schematicPost.getBuyCount();
        this.commentCount = schematicPost.getCommentCount();
        this.price = schematicPost.getPrice();
        this.star = schematicPost.getStar();
        this.size = schematicPost.getSize();
        this.createdAt = schematicPost.getCreatedAt();
        this.modifiedAt = schematicPost.getModifiedAt();
    }

	
}
