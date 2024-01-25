package com.daesoo.terracotta.member.dto;

import com.daesoo.terracotta.common.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {
	
	private Long id;
	
	private String userId;
	
	private String userName;
	
	private String email;
	
	private Boolean isActive;
	
	public static MemberResponseDto of(Member member) {
		return MemberResponseDto.builder()
				.id(member.getId())
				.userId(member.getUserId())
				.userName(member.getUsername())
				.email(member.getEmail())
				.isActive(member.isActive())
				.build();
	}

}
