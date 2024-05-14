package com.daesoo.terracotta.post.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileNameDto {

	private String schameticName;
	private ArrayList<String> imageNames;
	
	public static FileNameDto create(String schematicName, ArrayList<String> imageNames) {
		return FileNameDto.builder()
				.schameticName(schematicName)
				.imageNames(imageNames)
				.build();
	}
	
}
