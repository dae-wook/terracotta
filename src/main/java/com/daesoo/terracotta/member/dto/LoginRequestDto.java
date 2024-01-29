package com.daesoo.terracotta.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
	
	private String userId;
	
	private String password;

}