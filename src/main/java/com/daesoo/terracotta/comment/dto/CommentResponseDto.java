package com.daesoo.terracotta.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.common.entity.Comment;
import com.daesoo.terracotta.member.dto.MemberInfoResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
	
	private Long id;
	
	private Long schematicPostId;

    private String content;
    
    private MemberInfoResponseDto member;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;
    
    private List<ReplyResponseDto> replies;

    public static CommentResponseDto of(Comment comment) {
    	return CommentResponseDto.builder()
    			.id(comment.getId())
    			.schematicPostId(comment.getSchematicPost().getId())
    			.content(comment.getContent())
    			.member(MemberInfoResponseDto.of(comment.getMember()))
    			.replies(comment.getReplies() != null ? comment.getReplies().stream().map(ReplyResponseDto :: of).toList() : null)
    			.createdAt(comment.getCreatedAt())
    			.modifiedAt(comment.getModifiedAt())
    			.build();
    }
    
}
