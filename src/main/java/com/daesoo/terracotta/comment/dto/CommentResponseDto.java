package com.daesoo.terracotta.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.common.entity.Comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
	
	private Long id;
	
	private Long schematicPostId;

    private String content;
    
    private String memberName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;
    
    private List<ReplyResponseDto> replies;

    public static CommentResponseDto of(Comment comment) {
    	return CommentResponseDto.builder()
    			.id(comment.getId())
    			.schematicPostId(comment.getSchematicPost().getId())
    			.content(comment.getContent())
    			.memberName(comment.getMember().getMemberName())
    			.replies(comment.getReplies().stream().map(ReplyResponseDto :: of).toList())
    			.createdAt(comment.getCreatedAt())
    			.modifiedAt(comment.getModifiedAt())
    			.build();
    }
    
}
