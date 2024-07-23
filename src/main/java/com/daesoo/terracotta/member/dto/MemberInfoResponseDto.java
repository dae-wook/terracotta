package com.daesoo.terracotta.member.dto;

import com.daesoo.terracotta.common.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDto {
	
	String nickname;
	String introduction;
	String profileImage;
	
	public static MemberInfoResponseDto of(Member member) {
		return MemberInfoResponseDto.builder()
			.nickname(member.getMemberName())
			.introduction(member.getIntroduction())
			.profileImage(member.getProfileImage())
			.build();
	}

}
