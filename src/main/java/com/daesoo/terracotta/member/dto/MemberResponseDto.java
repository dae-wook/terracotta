package com.daesoo.terracotta.member.dto;

import java.util.List;

import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.notification.NotificationResponseDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {
	
	private Long id;
	
	private MemberInfoResponseDto member;
	
	private String email;
	
	private String token;
	
	private String ipAddress;
	
	public static MemberResponseDto of(Member member, String token, String ipAddress) {
		return MemberResponseDto.builder()
				.id(member.getId())
				.member(MemberInfoResponseDto.of(member))
				.email(member.getEmail())
				.token(token)
				.ipAddress(ipAddress)
				.build();
	}

}
