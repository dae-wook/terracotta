package com.daesoo.terracotta.notification.dto;

import com.daesoo.terracotta.common.dto.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {


    private String content;
    
    private NotificationType type;
    
    
    public static NotificationRequestDto create(String content,
    		NotificationType type
    		) {
    	return NotificationRequestDto.builder()
    			.content(content)
    			.type(type)
    			.build();
    }
	
}
