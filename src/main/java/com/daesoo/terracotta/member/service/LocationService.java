package com.daesoo.terracotta.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LocationService {

	private final WebClient webClient;
	
    public LocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://ipinfo.io").build();
    }

    public String getLocationByIp(String ipAddress) {
        return webClient.get()
                .uri("/" + ipAddress + "/geo")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
