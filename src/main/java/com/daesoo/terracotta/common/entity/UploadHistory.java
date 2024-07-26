package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.image.ImageType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "upload_histories")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadHistory extends TimeStamp{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;
	
	private ImageType imageType;
	
	@ManyToOne
	private Member member;

	public static UploadHistory create(String fileName, ImageType imageType, Member member) {
		
		return UploadHistory.builder()
				.fileName(fileName)
				.member(member)
				.imageType(imageType)
				.build();
		
	}


}
