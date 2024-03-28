package com.daesoo.terracotta.notification;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3737", "https://terracotta-nu.vercel.app"})
public class NotificationController {
	

	
	
}