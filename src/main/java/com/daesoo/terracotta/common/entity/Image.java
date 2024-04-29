package com.daesoo.terracotta.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String path;
	
	@ManyToOne
	private SchematicPost schematicPost;

	public static Image create(SchematicPost schematicPost, String imageName) {
		
		return Image.builder()
				.path(imageName)
				.schematicPost(schematicPost)
				.build();
	}
	
}
