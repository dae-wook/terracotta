package com.daesoo.terracotta.image;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ImageRequestDto {

	ImageType imageType;
	MultipartFile[] images;
	
}
