package com.daesoo.terracotta.member.dto;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.LoginHistory;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginHistoryResponseDto {
	
	private Long id;

	private String ipAddress;
	
	private Boolean success;
    
    private String location;
    
    private String email;
    
    private LocalDateTime createdAt;
	
	public static LoginHistoryResponseDto of(LoginHistory loginHistoy) {
		return LoginHistoryResponseDto.builder()
				.id(loginHistoy.getId())
	    		.ipAddress(loginHistoy.getIpAddress())
	    		.success(loginHistoy.getSuccess())
	    		.location(loginHistoy.getLocation())
	    		.email(loginHistoy.getEmail())
	    		.createdAt(loginHistoy.getCreatedAt())
	    		.build();
	}

}
