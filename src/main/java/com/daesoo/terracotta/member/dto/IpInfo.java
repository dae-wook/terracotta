package com.daesoo.terracotta.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpInfo {
	
    private String ip;
    private String city;
    private String region;
    private String country;

}
