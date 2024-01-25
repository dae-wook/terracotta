package com.daesoo.terracotta.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
	
	@NotBlank(message = "값이 필요합니다")
	private String userId;
	
	@NotEmpty(message = "값이 필요합니다")
	private String password;
	
	@NotBlank(message = "값이 필요합니다")
	private String username;
	
	@Email(message = "Email 형식이 아닙니다.")
	private String email;

}