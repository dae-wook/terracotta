package com.daesoo.terracotta.member.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.entity.EmailVerification;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.jwt.JwtUtil;
import com.daesoo.terracotta.common.repository.EmailVerificationRepository;
import com.daesoo.terracotta.common.repository.MemberRepository;
import com.daesoo.terracotta.common.util.MailUtil;
import com.daesoo.terracotta.member.dto.EmailRequestDto;
import com.daesoo.terracotta.member.dto.EmailVerificationRequestDto;
import com.daesoo.terracotta.member.dto.LoginRequestDto;
import com.daesoo.terracotta.member.dto.MemberResponseDto;
import com.daesoo.terracotta.member.dto.SignupRequestDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final EmailVerificationRepository emailVerificationRepository;
	private final JwtUtil jwtUtil;
	private final MailUtil mailUtil;

	@Transactional
	public MemberResponseDto signup(SignupRequestDto signupRequestDto) {
		
		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
		
		EmailVerification emailVerification = emailVerificationRepository.findByEmail(signupRequestDto.getEmail()).orElseThrow(
				() -> new IllegalArgumentException("인증 정보가 없습니다")
				);
		
		if(!emailVerification.isActive()) {
			throw new IllegalArgumentException("인증되지 않은 이메일입니다");
		}
		
		if(memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("중복 email");
		}
		
		if(memberRepository.findByMemberName(signupRequestDto.getNickname()).isPresent()) {
			throw new IllegalArgumentException("중복 username");
		}
		
		
		Member newMember = Member.create(signupRequestDto, encodedPassword);
		memberRepository.save(newMember);
		emailVerificationRepository.delete(emailVerification);
		
		return MemberResponseDto.of(newMember);
	}

	public MemberResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
		Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 유저 Email")
        );
		
		if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException("일치하지 않는 비밀번호");
		}
		
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getEmail()));
		
		return MemberResponseDto.of(member);
	}

	public Boolean existByEmail(String email) {

		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		
		if(optionalMember.isPresent()) {
			return false;
		}
		
		return true;
	}

	public Boolean existByUsername(String memberName) {

		Optional<Member> optionalMember = memberRepository.findByMemberName(memberName);
		
		if(optionalMember.isPresent()) {
			return false;
		}
		
		return true;
	}

	@Transactional
	public Boolean sendEmail(EmailRequestDto emailReqeustDto) {
		
		String email = emailReqeustDto.getEmail();
		String authCode = mailUtil.sendEmail(email);
		
		EmailVerification emailVerification = EmailVerification.create(email, authCode);
		
		emailVerificationRepository.save(emailVerification);
		
		return true;
	}

	@Transactional
	public Boolean emailVerification(EmailVerificationRequestDto emailRequestDto) {

		Optional<EmailVerification> optionalEmailVerification = emailVerificationRepository.findByEmailAndAuthCode(emailRequestDto.getEmail(), emailRequestDto.getAuthCode());
		
		if(!optionalEmailVerification.isPresent()) {
			throw new IllegalArgumentException("잘못된 인증요청입니다.");
		}
		
		EmailVerification emailVerification = optionalEmailVerification.get();
		if(LocalDateTime.now().isAfter(emailVerification.getExpireDate())) {
			emailVerificationRepository.delete(emailVerification);
			throw new IllegalArgumentException("인증기한이 만료되었습니다.");
		}
		
		emailVerification.active();
		
//		emailVerificationRepository.delete(emailVerification);
		
		return true;
	}

	
}
