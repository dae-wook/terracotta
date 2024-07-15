package com.daesoo.terracotta.comment.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.Reply;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyResponseDto {
	
	private Long id;
	
	private Long commentId;

    private String content;
    
    private String memberName;
    
    private String taggedMember;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;
    
    public static ReplyResponseDto of(Reply reply) {
    	return ReplyResponseDto.builder()
    			.id(reply.getId())
    			.commentId(reply.getComment().getId())
    			.content(reply.getContent())
    			.memberName(reply.getMember().getMemberName())
    			.taggedMember(reply.getTaggedMember())
    			.createdAt(reply.getCreatedAt())
    			.modifiedAt(reply.getModifiedAt())
    			.build();
    }
    
}
