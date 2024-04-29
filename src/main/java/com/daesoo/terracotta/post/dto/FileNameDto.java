package com.daesoo.terracotta.post.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileNameDto {

	private String schameticName;
	private ArrayList<String> imageNames;
	
}
