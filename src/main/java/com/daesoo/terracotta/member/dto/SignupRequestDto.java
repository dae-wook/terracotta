package com.daesoo.terracotta.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
	
	@Email(message = "Email 형식이 아닙니다.")
	private String memberId;
	
	@NotEmpty(message = "값이 필요합니다")
	private String password;
	
	@NotBlank(message = "값이 필요합니다")
	private String memberName;

}