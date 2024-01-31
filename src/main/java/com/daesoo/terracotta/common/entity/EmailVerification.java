package com.daesoo.terracotta.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "email_verification")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerification extends TimeStamp{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String authCode;
    
    @Column(nullable = false)
    private LocalDateTime expireDate;
    
    @Column(name = "is_active", columnDefinition = "boolean default false")
    private boolean isActive;
    
    public static EmailVerification create(String email, String authCode) {
    	return EmailVerification.builder()
    			.email(email)
    			.authCode(authCode)
    			.expireDate(LocalDateTime.now().plusMinutes(5))
    			.build();
    }
    
    public void active() {
    	this.isActive = true;
    }
}
