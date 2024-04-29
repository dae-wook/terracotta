package com.daesoo.terracotta.post.dto;

import com.daesoo.terracotta.common.entity.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageResponseDto {

	private Long id;
	private String image;
	
	public static ImageResponseDto of(Image image) {
		return ImageResponseDto.builder()
				.id(image.getId())
				.image(image.getPath())
				.build();
	}
	
}
