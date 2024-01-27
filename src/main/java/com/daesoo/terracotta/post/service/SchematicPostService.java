package com.daesoo.terracotta.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.entity.Tag;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.common.repository.TagRepository;
import com.daesoo.terracotta.common.util.FileUtil;
import com.daesoo.terracotta.post.dto.SchematicPostRequestDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;
import com.daesoo.terracotta.schematic.util.Schematic;
import com.daesoo.terracotta.schematic.util.SchemeParser;
import com.google.cloud.storage.Storage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchematicPostService {
	
	private final SchematicPostRepository schematicPostRepository;
	private final TagRepository tagRepository;
	private final Storage storage;
	private final FileUtil fileUtil;
	private final SchemeParser schemParser;

	
	@Transactional
	public SchematicPostResponseDto createSchematicPost(SchematicPostRequestDto schematicPostRequestDto, Member member) {
		

		String filePath = fileUtil.uploadFile(schematicPostRequestDto.getFile(), schematicPostRequestDto.getFile().getContentType());
		SchematicPost schematicPost = SchematicPost.create(schematicPostRequestDto, filePath, member);
		
		List<Tag> tags = tagRepository.findAllById(schematicPostRequestDto.getTags());
		for(Tag tag: tags) {
			schematicPost.addPostTag(tag);
		}
		Schematic schematic = schemParser.getSchematic(schematicPost.getFilePath());
		schematicPostRepository.save(schematicPost);
		
		return SchematicPostResponseDto.of(schematicPost, schematic);
	}


	public SchematicPostResponseDto getSchematicPost(Long schematicPostId) {
		SchematicPost schematicPost = schematicPostRepository.findById(schematicPostId).orElseThrow(
				() -> new NullPointerException("dd")
				);
		Schematic schematic = schemParser.getSchematic(schematicPost.getFilePath());
		
		
		return SchematicPostResponseDto.of(schematicPost, schematic);
	}

}
