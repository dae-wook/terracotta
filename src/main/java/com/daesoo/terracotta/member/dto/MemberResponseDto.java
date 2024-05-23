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
	
	private String email;
	
	private String nickname;
	
	private String token;
	
	private List<NotificationResponseDto> notifications;
	
	private String ipAddress;
	
	public static MemberResponseDto of(Member member, String token, String ipAddress) {
		return MemberResponseDto.builder()
				.id(member.getId())
				.email(member.getEmail())
				.nickname(member.getMemberName())
				.token(token)
				.ipAddress(ipAddress)
				.notifications(member.getNotifications().stream().map(NotificationResponseDto :: of).toList())
				.build();
	}

}
