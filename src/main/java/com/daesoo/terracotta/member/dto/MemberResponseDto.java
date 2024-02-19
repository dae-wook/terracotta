package com.daesoo.terracotta.member.dto;

import com.daesoo.terracotta.common.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {
	
	private Long id;
	
	private String email;
	
	private String nickname;
	
	private String token;
	
	public static MemberResponseDto of(Member member, String token) {
		return MemberResponseDto.builder()
				.id(member.getId())
				.email(member.getEmail())
				.nickname(member.getMemberName())
				.token(token)
				.build();
	}

}
