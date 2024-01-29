package com.daesoo.terracotta.member.service;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.jwt.JwtUtil;
import com.daesoo.terracotta.common.repository.MemberRepository;
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
	private final JwtUtil jwtUtil;

	@Transactional
	public MemberResponseDto signup(SignupRequestDto signupRequestDto) {
		
		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
		
		if(memberRepository.findByMemberId(signupRequestDto.getMemberId()).isPresent()) {
			throw new IllegalArgumentException("중복 userId");
		}
		
		if(memberRepository.findByMemberName(signupRequestDto.getMemberName()).isPresent()) {
			throw new IllegalArgumentException("중복 username");
		}
		
		Member newMember = Member.create(signupRequestDto, encodedPassword);
		memberRepository.save(newMember);
		
		return MemberResponseDto.of(newMember);
	}

	public MemberResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
		Member member = memberRepository.findByMemberId(loginRequestDto.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 유저 ID")
        );
		
		if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException("일치하지 않는 비밀번호");
		}
		
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getMemberId()));
		
		return MemberResponseDto.of(member);
	}

	public Boolean existByUserId(String memberId) {

		Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
		
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
	
}
