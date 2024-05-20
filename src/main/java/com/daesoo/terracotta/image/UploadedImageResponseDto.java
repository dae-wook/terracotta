package com.daesoo.terracotta.image;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedImageResponseDto {

	private ArrayList<String> imageNames;
	
	public static UploadedImageResponseDto of (ArrayList<String> imageNames) {
		return UploadedImageResponseDto.builder()
				.imageNames(imageNames)
				.build();
	}
	
}
