package com.daesoo.terracotta.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
	
	@Email(message = "Email 형식이 아닙니다.")
	private String email;
	
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,15}$", message = "잘못된 양식의 비밀번호")
	private String password;
	
	@Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,10}$", message = "잘못된 양식의 닉네임")
	private String nickname;

}