package com.daesoo.terracotta.notification;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.dto.NotificationType;
import com.daesoo.terracotta.common.entity.Notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponseDto {

	private Long id;
	
	private String content;
	
	private NotificationType type;
	
	private boolean isRead;
	
	private LocalDateTime createdAt;
	
	
	public static NotificationResponseDto of(Notification notification) {
		
		return NotificationResponseDto.builder()
				.id(notification.getId())
				.content(notification.getContent())
				.type(notification.getType())
				.isRead(notification.isRead())
				.createdAt(notification.getCreatedAt())
				.build();
	}
	
}
