package com.daesoo.terracotta.comment.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.Reply;
import com.daesoo.terracotta.member.dto.MemberInfoResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyResponseDto {
	
	private Long id;
	
	private Long commentId;

    private String content;
    
    private MemberInfoResponseDto member;
    
    private String taggedMember;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;
    
    public static ReplyResponseDto of(Reply reply) {
    	return ReplyResponseDto.builder()
    			.id(reply.getId())
    			.commentId(reply.getComment().getId())
    			.content(reply.getContent())
    			.member(MemberInfoResponseDto.of(reply.getMember()))
    			.taggedMember(reply.getTaggedMember())
    			.createdAt(reply.getCreatedAt())
    			.modifiedAt(reply.getModifiedAt())
    			.build();
    }
    
}
