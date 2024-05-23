package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.member.dto.IpInfo;
import com.daesoo.terracotta.member.dto.MemberResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "login_history")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistory extends TimeStamp{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 45, nullable = false)
	private String ipAddress;
	
	@Column(nullable = false)
	private Boolean success;
	
    @Column(length = 255)
    private String deviceInfo;
    
    @Column(length = 100)
    private String location;
    
    @Column(nullable = false)
    private String email;
    
    public static LoginHistory create(MemberResponseDto memberResponseDto, IpInfo ipInfo) {
    	return LoginHistory.builder()
    		.ipAddress(ipInfo.getIp())
    		.success(true)
    		.location(ipInfo.getCountry() + "/" + ipInfo.getCity())
    		.email(memberResponseDto.getEmail())
    		.build();
    	
    }
	
}
