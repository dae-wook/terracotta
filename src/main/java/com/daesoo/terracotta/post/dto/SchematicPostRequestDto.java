package com.daesoo.terracotta.post.dto;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchematicPostRequestDto {

	
    private String title;

    private String content;
    
    private int price;

    private ArrayList<Long> tags;
    
    private MultipartFile file;
    
    private MultipartFile image;
	
}
