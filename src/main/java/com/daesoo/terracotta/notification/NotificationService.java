package com.daesoo.terracotta.notification;

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
	};
	
}
