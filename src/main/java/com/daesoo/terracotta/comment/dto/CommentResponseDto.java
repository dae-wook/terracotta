package com.daesoo.terracotta.comment.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.Comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
	
	private Long id;
	
	private Long schematicPostId;

    private String content;
    
    private float star;
    
    private String memberName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;

    public static CommentResponseDto of(Comment comment) {
    	return CommentResponseDto.builder()
    			.id(comment.getId())
    			.schematicPostId(comment.getSchematicPost().getId())
    			.content(comment.getContent())
    			.star(comment.getStar())
    			.memberName(comment.getMember().getMemberName())
    			.createdAt(comment.getCreatedAt())
    			.modifiedAt(comment.getModifiedAt())
    			.build();
    }
    
}
