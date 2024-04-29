package com.daesoo.terracotta.member.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.EmailVerification;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.exception.DuplicationException;
import com.daesoo.terracotta.common.jwt.JwtUtil;
import com.daesoo.terracotta.common.repository.CommentRepository;
import com.daesoo.terracotta.common.repository.EmailVerificationRepository;
import com.daesoo.terracotta.common.repository.MemberRepository;
import com.daesoo.terracotta.common.repository.PostTagRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.common.util.MailUtil;
import com.daesoo.terracotta.member.dto.EmailRequestDto;
import com.daesoo.terracotta.member.dto.EmailVerificationRequestDto;
import com.daesoo.terracotta.member.dto.LoginRequestDto;
import com.daesoo.terracotta.member.dto.MemberResponseDto;
import com.daesoo.terracotta.member.dto.SignupRequestDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

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
	private final CommentRepository commentRepository;
	private final SchematicPostRepository schematicPostRepository;
	private final PostTagRepository postTagRepository;
	private final JwtUtil jwtUtil;
	private final MailUtil mailUtil;

	@Transactional
	public MemberResponseDto signup(SignupRequestDto signupRequestDto, HttpServletResponse response) {
		
		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
		
		if(memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
			throw new DuplicationException(ErrorMessage.EMAIL_DUPLICATION.getMessage());
		}
		
		if(memberRepository.findByMemberName(signupRequestDto.getNickname()).isPresent()) {
			throw new DuplicationException(ErrorMessage.USERNAME_DUPLICATION.getMessage());
		}
		
		EmailVerification emailVerification = emailVerificationRepository.findByEmail(signupRequestDto.getEmail()).orElseThrow(
				() -> new IllegalArgumentException(ErrorMessage.EMAIL_AUTH_INFO_NOT_FOUND.getMessage())
				);
		
		if(!emailVerification.isActive()) {
			throw new IllegalArgumentException(ErrorMessage.EMAIL_NOT_VERIFIED.getMessage());
		}
		
		
		Member newMember = Member.create(signupRequestDto, encodedPassword);
		memberRepository.save(newMember);
		emailVerificationRepository.delete(emailVerification);
		String token = jwtUtil.createToken(newMember.getEmail());
		
		return MemberResponseDto.of(newMember, token);
	}

	public MemberResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
		Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessage.WRONG_EMAIL_OR_PASSWORD.getMessage())
        );
		
		if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException(ErrorMessage.WRONG_EMAIL_OR_PASSWORD.getMessage());
		}
		
//		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getEmail()));
		String token = jwtUtil.createToken(member.getEmail());
		
		return MemberResponseDto.of(member, token);
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
	public LocalDateTime sendEmail(EmailRequestDto emailReqeustDto) {
		
		List<EmailVerification> emailVerifications = emailVerificationRepository.findAllByEmail(emailReqeustDto.getEmail());
		
		//이전 인증정보가 남아있다면 삭제
		if(emailVerifications.size() > 0) {
			emailVerificationRepository.deleteAll(emailVerifications);
		}
		
		if(memberRepository.findByEmail(emailReqeustDto.getEmail()).isPresent()) {
			throw new IllegalArgumentException(ErrorMessage.EMAIL_DUPLICATION.getMessage());
		}
		
		String email = emailReqeustDto.getEmail();
		String authCode = mailUtil.sendEmail(email);
		
		EmailVerification emailVerification = EmailVerification.create(email, authCode);
		
		emailVerificationRepository.save(emailVerification);
		
		return emailVerification.getExpireDate();
	}

//	@Transactional
//	public Boolean emailVerification(EmailVerificationRequestDto emailRequestDto) {
//
//		Optional<EmailVerification> optionalEmailVerification = emailVerificationRepository.findByEmailAndAuthCode(emailRequestDto.getEmail(), emailRequestDto.getAuthCode());
//		
//		if(!optionalEmailVerification.isPresent()) {
//			throw new IllegalArgumentException(ErrorMessage.INVALID_AUTHENTICATION_REQUEST.getMessage());
//		}
//		
//		EmailVerification emailVerification = optionalEmailVerification.get();
//		if(LocalDateTime.now().isAfter(emailVerification.getExpireDate())) {
//			throw new IllegalArgumentException(ErrorMessage.EXPIRED_AUTHENTICATION.getMessage());
//		}
//		
//		emailVerification.active();
//		
//		return true;
//	}
	
	@Transactional
	public Boolean emailVerification(EmailVerificationRequestDto emailRequestDto) {

		Optional<EmailVerification> optionalEmailVerification = emailVerificationRepository.findByEmail(emailRequestDto.getEmail());
		
		if(!optionalEmailVerification.isPresent()) {
			throw new IllegalArgumentException(ErrorMessage.EMAIL_AUTH_INFO_NOT_FOUND.getMessage());
		}
		
		EmailVerification emailVerification = optionalEmailVerification.get();
		
		if(!emailVerification.getAuthCode().equals(emailRequestDto.getAuthCode())) {
			throw new IllegalArgumentException(ErrorMessage.AUTHENTICATION_CODE_MISSMATCH.getMessage());
		}
		
		if(LocalDateTime.now().isAfter(emailVerification.getExpireDate())) {
			throw new IllegalArgumentException(ErrorMessage.EXPIRED_AUTHENTICATION.getMessage());
		}
		
		emailVerification.active();
		
		return true;
	}

	public Page<CommentResponseDto> getCommentListByLoginMember(Member member, Integer page, Integer size) {
		
//		commentRepository.findByMemberMemberId(member.getId());
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		return commentRepository.findByMemberId(pageable, member.getId()).map(CommentResponseDto::of);
	}

	
	@Transactional
	public String resign(Member user) {
		// TODO Auto-generated method stub
		memberRepository.delete(user);
		return "탈퇴 성공";
	}

	public Page<SchematicPostResponseDto> getSchematicPostListByLoginMember(Member user, Integer page, Integer size, Long[] tags) {
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

		if(tags[0] == 0) {
			return schematicPostRepository.findAllByMember(pageable, user).map(SchematicPostResponseDto::of);
		}

		return postTagRepository.findPostsByTagsAndMember(pageable, tags, tags.length, user).map(SchematicPostResponseDto::of);
	}

	
}
