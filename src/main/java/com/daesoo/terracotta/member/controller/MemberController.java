package com.daesoo.terracotta.member.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.exception.UnauthorizedException;
import com.daesoo.terracotta.member.UserDetailsImpl;
import com.daesoo.terracotta.member.dto.EmailRequestDto;
import com.daesoo.terracotta.member.dto.EmailVerificationRequestDto;
import com.daesoo.terracotta.member.dto.LoginRequestDto;
import com.daesoo.terracotta.member.dto.MemberResponseDto;
import com.daesoo.terracotta.member.dto.PasswordResetRequestDto;
import com.daesoo.terracotta.member.dto.SignupRequestDto;
import com.daesoo.terracotta.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@CrossOrigin(origins = {"http://localhost:3737", "https://terracotta-nu.vercel.app"})
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping("/signup")
	public ResponseDto<MemberResponseDto> signup(
			@Valid @RequestBody SignupRequestDto signupRequestDto,
			HttpServletResponse response) {
		
		return ResponseDto.success(HttpStatus.CREATED, memberService.signup(signupRequestDto, response));
	}
	
	@DeleteMapping("/resign")
	public ResponseDto<String> resign(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		return ResponseDto.success(HttpStatus.CREATED, memberService.resign(userDetails.getUser()));
	}
	
	@PostMapping("/password/forgot")
    public ResponseDto<LocalDateTime> forgotPassword(@RequestBody EmailRequestDto emailRequestDto) {
        
        return ResponseDto.success(HttpStatus.OK, memberService.sendPasswordResetEmail(emailRequestDto.getEmail()));
    }
	
	@PostMapping("/password/reset")
    public ResponseDto<Boolean> resetPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) {
        
        return ResponseDto.success(HttpStatus.OK, memberService.resetPassword(passwordResetRequestDto));
    }
	
	@PostMapping("/login")
	public ResponseDto<MemberResponseDto> login(
			@RequestBody LoginRequestDto loginRequestDto,
			HttpServletRequest request,
			HttpServletResponse response) {
		return ResponseDto.success(HttpStatus.OK, memberService.login(loginRequestDto, response, getClientIp(request)));
	}
	
	@GetMapping("/nickname/check/{nickname}")
	public ResponseDto<Boolean> checkUsernameExists(
			@PathVariable("nickname") String nickname) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.existByUsername(nickname));
	}
	
	@GetMapping("/email/check/{email}")
	public ResponseDto<Boolean> checkEmailExists(
			@PathVariable("email") String email) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.existByEmail(email));
	}
	
	@PostMapping("/email/send")
	public ResponseDto<LocalDateTime> sendEmail(
			@Valid @RequestBody EmailRequestDto emailRequestDto) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.sendEmail(emailRequestDto));
	}
	
	@PostMapping("/email/verification")
	public ResponseDto<Boolean> emailVerification(
			@Valid @RequestBody EmailVerificationRequestDto emailRequestDto) {
		
		return ResponseDto.success(HttpStatus.OK, memberService.emailVerification(emailRequestDto));
	}
	
	
	public String getClientIp(HttpServletRequest request) {
	    String ipAddress = request.getHeader("X-Forwarded-For");
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("Proxy-Client-IP");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getRemoteAddr();
	    }
	    
	    if (ipAddress.contains(":")) {
            ipAddress = "1.235.236.122"; // 기본값 설정 또는 처리 로직 추가
        }
	    return ipAddress;
	}
	
	
	
	
}