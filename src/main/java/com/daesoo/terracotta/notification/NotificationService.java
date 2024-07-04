package com.daesoo.terracotta.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.Notification;
import com.daesoo.terracotta.common.repository.NotificationRepository;
import com.daesoo.terracotta.notification.dto.NotificationRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	
	@Transactional
	public Boolean createNotification(NotificationRequestDto dto, Member member) {
		
		Notification notification = Notification.create(dto, member);
		
		notificationRepository.save(notification);
		
		return true;
	}
	
	@Transactional
	public Boolean createReplyNotification(NotificationRequestDto dto, Member schematicPostMember, Member commentMember, Member replyMember, Set<Member> targetMembers) {

		List<Notification> notifications = new ArrayList<>();
		for(Member member : targetMembers) {
			if(!replyMember.equals(member)) {
				System.out.println(member.getMemberName());
				Notification notification = Notification.create(dto, member);
				notifications.add(notification);
			}
		}
		
		notificationRepository.saveAll(notifications);
		
		return true;
	}

	@Transactional
	public int deleteAllReadNotificationsByLoginMember(Member user) {

		return notificationRepository.deleteByIsReadTrueAndMember(user);
	}
	
}
