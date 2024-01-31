package com.daesoo.terracotta.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.member.dto.EmailRequestDto;
import com.daesoo.terracotta.member.dto.EmailVerificationRequestDto;
import com.daesoo.terracotta.member.dto.LoginRequestDto;
import com.daesoo.terracotta.member.dto.MemberResponseDto;
import com.daesoo.terracotta.member.dto.SignupRequestDto;
import com.daesoo.terracotta.member.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping("/signup")
	public ResponseDto<MemberResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		
		return ResponseDto.success(HttpStatus.CREATED, memberService.signup(signupRequestDto));
	}
	
	@PostMapping("/login")
	public ResponseDto<MemberResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
		return ResponseDto.success(HttpStatus.OK, memberService.login(loginRequestDto, response));
	}
	
	@GetMapping("/check/member-email/{email}")
	public ResponseDto<Boolean> checkUserIdExists(@PathVariable("email") String memberId) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.existByEmail(memberId));
	}
	
	@GetMapping("/check/member-name/{memberName}")
	public ResponseDto<Boolean> checkUsernameExists(@PathVariable("memberName") String memberName) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.existByUsername(memberName));
	}
	
	@PostMapping("send-email")
	public ResponseDto<Boolean> sendEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.sendEmail(emailRequestDto));
	}
	
	@PostMapping("email-verification")
	public ResponseDto<Boolean> emailVerification(@Valid @RequestBody EmailVerificationRequestDto emailRequestDto) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.emailVerification(emailRequestDto));
	}
	
	
	
}