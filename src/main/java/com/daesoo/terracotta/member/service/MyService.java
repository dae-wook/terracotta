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
import com.daesoo.terracotta.common.repository.LoginHistoryRepository;
import com.daesoo.terracotta.common.repository.MemberRepository;
import com.daesoo.terracotta.common.repository.PostTagRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.common.util.MailUtil;
import com.daesoo.terracotta.member.dto.EmailRequestDto;
import com.daesoo.terracotta.member.dto.EmailVerificationRequestDto;
import com.daesoo.terracotta.member.dto.LoginHistoryResponseDto;
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
public class MyService {
	
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final EmailVerificationRepository emailVerificationRepository;
	private final CommentRepository commentRepository;
	private final SchematicPostRepository schematicPostRepository;
	private final PostTagRepository postTagRepository;
	private final LoginHistoryRepository loginHistoryRepository;
	private final JwtUtil jwtUtil;
	private final MailUtil mailUtil;

	

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

	public Page<LoginHistoryResponseDto> getLoginHistory(Member user, Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		
		return loginHistoryRepository.findAllByEmail(pageable, user.getEmail()).map(LoginHistoryResponseDto::of);
	}

	
	
	
}
