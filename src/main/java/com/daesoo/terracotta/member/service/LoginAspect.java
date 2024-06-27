package com.daesoo.terracotta.member.service;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.daesoo.terracotta.common.entity.LoginHistory;
import com.daesoo.terracotta.common.repository.LoginHistoryRepository;
import com.daesoo.terracotta.member.dto.IpInfo;
import com.daesoo.terracotta.member.dto.LoginRequestDto;
import com.daesoo.terracotta.member.dto.MemberResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LoginAspect {


	private final LocationService locationService;
	private final LoginHistoryRepository loginHistoryRepository;

    @AfterReturning(
            pointcut = "execution(* com.daesoo.terracotta.member.service.MemberService.login(..))",
            returning = "result")
    public void logSuccessfulLogin(MemberResponseDto result) {


		ObjectMapper mapper = new ObjectMapper();
		IpInfo ipInfo = null;
		

		try {
			ipInfo = mapper.readValue(locationService.getLocationByIp(result.getIpAddress()), IpInfo.class);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		LoginHistory loginHistory = LoginHistory.create(result, ipInfo);
		
		loginHistoryRepository.save(loginHistory);
    }
}
