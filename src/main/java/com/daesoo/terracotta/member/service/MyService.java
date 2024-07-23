package com.daesoo.terracotta.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daesoo.terracotta.comment.dto.CommentResponseDto;
import com.daesoo.terracotta.comment.dto.MyCommentResponseDto;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.Notification;
import com.daesoo.terracotta.common.jwt.JwtUtil;
import com.daesoo.terracotta.common.repository.CommentRepository;
import com.daesoo.terracotta.common.repository.EmailVerificationRepository;
import com.daesoo.terracotta.common.repository.LoginHistoryRepository;
import com.daesoo.terracotta.common.repository.MemberRepository;
import com.daesoo.terracotta.common.repository.NotificationRepository;
import com.daesoo.terracotta.common.repository.PostTagRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.common.util.FileUtil;
import com.daesoo.terracotta.common.util.MailUtil;
import com.daesoo.terracotta.member.dto.LoginHistoryResponseDto;
import com.daesoo.terracotta.notification.NotificationResponseDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

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
	private final NotificationRepository notificationRepository;
	private final JwtUtil jwtUtil;
	private final MailUtil mailUtil;
	private final FileUtil fileUtil;

	

	public Page<MyCommentResponseDto> getCommentListByLoginMember(Member member, Integer page, Integer size) {
		
//		commentRepository.findByMemberMemberId(member.getId());
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		return commentRepository.findByMemberId(pageable, member.getId()).map(MyCommentResponseDto::of);
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
	public Page<NotificationResponseDto> getNotificationByLoginMember(Integer page, Integer size, Member user) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		Page<Notification> notifications = notificationRepository.findAllByMember(pageable, user);
		
		for(Notification notification : notifications.get().toList()) {
			notification.read();
		}
		
		return notifications.map(NotificationResponseDto :: of); 
	}


	@Transactional
	public String updateProfileImage(MultipartFile image, Member user) {

		
		String fileName = fileUtil.uploadProfileImage(user.getId(), image);
		
		user.updateProfileImage(fileName);
		memberRepository.save(user);
		
		return fileName;
	}

	
	
	
}
