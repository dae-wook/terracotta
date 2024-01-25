package com.daesoo.terracotta.member;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.MemberRepository;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.member.dto.MemberResponseDto;
import com.daesoo.terracotta.member.dto.SignupRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	@Transactional
	public MemberResponseDto signup(SignupRequestDto signupRequestDto) {
		
		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
		
		if(memberRepository.findByUserId(signupRequestDto.getUserId()).isPresent()) {
			throw new IllegalArgumentException("중복 userId");
		}
		
		if(memberRepository.findByUsername(signupRequestDto.getUsername()).isPresent()) {
			throw new IllegalArgumentException("중복 username");
		}
		
		if(memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("중복 email");
		}
		
		Member newMember = Member.create(signupRequestDto, encodedPassword);
		memberRepository.save(newMember);
		
		return MemberResponseDto.of(newMember);
	}
	
}
