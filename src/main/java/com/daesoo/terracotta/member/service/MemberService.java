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
import org.springframework.web.multipart.MultipartFile;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.EmailVerification;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.exception.DuplicationException;
import com.daesoo.terracotta.common.jwt.JwtUtil;
import com.daesoo.terracotta.common.repository.CommentRepository;
import com.daesoo.terracotta.common.repository.EmailVerificationRepository;
import com.daesoo.terracotta.common.repository.LoginHistoryRepository;
import com.daesoo.terracotta.common.repository.MemberRepository;
import com.daesoo.terracotta.common.repository.PostTagRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.common.util.FileUtil;
import com.daesoo.terracotta.common.util.MailUtil;
import com.daesoo.terracotta.member.dto.EmailRequestDto;
import com.daesoo.terracotta.member.dto.EmailVerificationRequestDto;
import com.daesoo.terracotta.member.dto.InfoRequestDto;
import com.daesoo.terracotta.member.dto.InfoResponseDto;
import com.daesoo.terracotta.member.dto.LoginHistoryResponseDto;
import com.daesoo.terracotta.member.dto.LoginRequestDto;
import com.daesoo.terracotta.member.dto.MemberInfoResponseDto;
import com.daesoo.terracotta.member.dto.MemberResponseDto;
import com.daesoo.terracotta.member.dto.PasswordChangeRequestDto;
import com.daesoo.terracotta.member.dto.PasswordResetRequestDto;
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
	private final CommentRepository commentRepository;
	private final SchematicPostRepository schematicPostRepository;
	private final PostTagRepository postTagRepository;
	private final LoginHistoryRepository loginHistoryRepository;
	private final FileUtil fileUtil;
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
		String token = jwtUtil.createToken(newMember);
		
		return MemberResponseDto.of(newMember, token, null);
	}

	public MemberResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response, String ip) {
		Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessage.WRONG_EMAIL_OR_PASSWORD.getMessage())
        );
		
		if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException(ErrorMessage.WRONG_EMAIL_OR_PASSWORD.getMessage());
		}
		

		
		String token = jwtUtil.createToken(member);
		
		return MemberResponseDto.of(member, token, ip);
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
	
	public Page<LoginHistoryResponseDto> getLoginHistory(Member user, Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		
		return loginHistoryRepository.findAllByEmail(pageable, user.getEmail()).map(LoginHistoryResponseDto::of);
	}
	
	@Transactional
	public String updateIntroduction(Member user, String introduction) {
		// TODO Auto-generated method stub
		
		if(introduction.length() > 500) {
			throw new IllegalArgumentException(ErrorMessage.INTRODUCTION_TOO_LONG.getMessage());
		}
		
		user.updateIntroduction(introduction);
		
		return user.getIntroduction();
	}
	
	@Transactional
	public String updateProfileImage(MultipartFile image, Member user) {

		
		String fileName = fileUtil.uploadProfileImage(user.getId(), image);
		
		user.updateProfileImage(fileName);
		memberRepository.save(user);
		
		return fileName;
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

	
	@Transactional
	public String resign(Member user) {
		// TODO Auto-generated method stub
		memberRepository.delete(user);
		return "탈퇴 성공";
	}

	@Transactional
	public LocalDateTime sendPasswordResetEmail(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
		
		//비밀번호 리셋 메일을 보내고 1분이 지나지 않았을때 예외처리(짧은시간에 여러번 보내는 것 방지)
		if(member.getPasswordResetExpiry() != null && LocalDateTime.now().isBefore(member.getPasswordResetExpiry().minusMinutes(4))) {
			throw new IllegalArgumentException(ErrorMessage.INVALID_AUTHENTICATION_REQUEST.getMessage());
		}
		
		return member.passwordResetSetting(mailUtil.sendResetPasswordEmail(email));
		
	}

	@Transactional
	public Boolean resetPassword(PasswordResetRequestDto passwordResetRequestDto) {
		// TODO Auto-generated method stub
		
		Member member = memberRepository.findByPasswordResetKey(passwordResetRequestDto.getKey()).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
		
		if(member.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException(ErrorMessage.EXPIRED_AUTHENTICATION.getMessage());
		}
		
		if(passwordEncoder.matches(passwordResetRequestDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException(ErrorMessage.SAME_PASSWORD.getMessage());
		}
		
		String encodedPassword = passwordEncoder.encode(passwordResetRequestDto.getPassword());
		
		member.resetPassword(encodedPassword);
		
		member.clearResetPasswordInfo();
		
		return true;
	}

	@Transactional
	public Boolean changePassword(Member member, PasswordChangeRequestDto passwordChangeRequestDto) {

		if(!passwordEncoder.matches(passwordChangeRequestDto.getOldPassword(), member.getPassword())) {
			throw new BadCredentialsException(ErrorMessage.WRONG_PASSWORD.getMessage());
		}
		
		String encodedPassword = passwordEncoder.encode(passwordChangeRequestDto.getNewPassword());
		
		member.resetPassword(encodedPassword);
		
		return true;
	}

	public MemberInfoResponseDto getMemberInfo(String nickname) {
		
		Member member = memberRepository.findByMemberName(nickname).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage())
				);
		
		return MemberInfoResponseDto.of(member);
	}

	
	@Transactional
	public InfoResponseDto updateInfoByLoginMember(Member user, InfoRequestDto dto) {

		if (dto == null) {
	        throw new IllegalArgumentException(ErrorMessage.NULL_DTO.getMessage());
	    }
		
		user.updateInfo(dto);
		
		memberRepository.save(user);
		
		return InfoResponseDto.of(user);
	}

	

	
	
	
}
