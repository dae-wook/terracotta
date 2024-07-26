package com.daesoo.terracotta.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.daesoo.terracotta.common.entity.Comment;
import com.daesoo.terracotta.member.dto.MemberInfoResponseDto;
import com.daesoo.terracotta.post.dto.SchematicPostListResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyCommentResponseDto {
	
	private Long id;
	
	private SchematicPostListResponseDto schematicPost;

    private String content;
    
    private MemberInfoResponseDto member;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;
    
    private List<ReplyResponseDto> replies;

    public static MyCommentResponseDto of(Comment comment) {
    	return MyCommentResponseDto.builder()
    			.id(comment.getId())
    			.schematicPost(SchematicPostListResponseDto.of(comment.getSchematicPost()))
    			.content(comment.getContent())
    			.member(MemberInfoResponseDto.of(comment.getMember()))
    			.replies(comment.getReplies() != null ? comment.getReplies().stream().map(ReplyResponseDto :: of).toList() : null)
    			.createdAt(comment.getCreatedAt())
    			.modifiedAt(comment.getModifiedAt())
    			.build();
    }
    
}
