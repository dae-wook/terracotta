package com.daesoo.terracotta.member.dto;

import com.daesoo.terracotta.common.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoResponseDto {
	
	private String introduction;
	
	private String profileImage;
	
	private String server;
	
	private String village;
	
	public static InfoResponseDto of(Member member) {
		return InfoResponseDto.builder()
				.introduction(member.getIntroduction())
				.profileImage(member.getProfileImage())
				.server(member.getServer())
				.village(member.getVillage())
				.build();
	}

}