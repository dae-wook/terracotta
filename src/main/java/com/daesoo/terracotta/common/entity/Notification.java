package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.common.dto.NotificationType;
import com.daesoo.terracotta.notification.dto.NotificationRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name = "notifications")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends TimeStamp{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    private boolean isRead;
    
    @ManyToOne
    private Member member;
    
    public static Notification create(NotificationRequestDto dto, Member member) {
    	return Notification.builder()
    			.content(dto.getContent())
    			.type(dto.getType())
    			.member(member)
    			.build();
    	
    	    	
    }
    
    
}
