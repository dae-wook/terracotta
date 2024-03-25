package com.daesoo.terracotta.post.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.entity.Tag;
import com.daesoo.terracotta.common.repository.PostTagRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.common.repository.TagRepository;
import com.daesoo.terracotta.common.util.FileUtil;
import com.daesoo.terracotta.post.dto.SchematicPostListResponseDto;
import com.daesoo.terracotta.post.dto.SchematicPostRequestDto;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;
import com.daesoo.terracotta.schematic.util.SchematicDto;
import com.daesoo.terracotta.schematic.util.SchemeParser;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchematicPostService {
	
	private final SchematicPostRepository schematicPostRepository;
	private final PostTagRepository postTagRepository;
	private final TagRepository tagRepository;
	private final FileUtil fileUtil;
	private final SchemeParser schemParser;

	
	@Transactional
	public SchematicPostResponseDto createSchematicPost(SchematicPostRequestDto schematicPostRequestDto, Member member) {
		

		String schematicJson ="";
		String originalFilename = schematicPostRequestDto.getFile().getOriginalFilename();
		try {
			schematicJson = schemParser.convertFileToSchematicJson(schematicPostRequestDto.getFile().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] filePath = fileUtil.uploadFile(schematicJson, schematicPostRequestDto.getFile(), schematicPostRequestDto.getImage());
		
//		System.out.println(schematicJson);
		SchematicPost schematicPost = SchematicPost.create(schematicPostRequestDto, filePath, member);
		
		List<Tag> tags = tagRepository.findAllById(schematicPostRequestDto.getTags());
		for(Tag tag: tags) {
			schematicPost.addPostTag(tag);
		}
//		Schematic schematic = schemParser.getSchematic(schematicPost.getFilePath());
		schematicPostRepository.save(schematicPost);
		
		return SchematicPostResponseDto.of(schematicPost);
	}


	public SchematicPostResponseDto getSchematicPost(Long schematicPostId) {
		SchematicPost schematicPost = schematicPostRepository.findById(schematicPostId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage())
				);
		
		
		return SchematicPostResponseDto.of(schematicPost);
	}
	
	public SchematicDto getSchematic(Long schematicPostId) {
		SchematicPost schematicPost = schematicPostRepository.findById(schematicPostId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage())
				);
		
		
		return fileUtil.downloadSchematicFileToSchematicDto(schematicPost.getFilePath());
	}


	public Page<SchematicPostListResponseDto> getSchematicPostList(Integer page, Integer size, Long[] tags) {
		
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		if(tags[0] == 0) {
			return schematicPostRepository.findAll(pageable).map(SchematicPostListResponseDto::of);
		}

		return postTagRepository.findPostsByTags(pageable, tags, tags.length).map(SchematicPostListResponseDto::of);
	}


	@Transactional
	public Boolean deleteSchematic(Long schematicPostId, Member user) {
		// TODO Auto-generated method stub
		
		SchematicPost schematic = schematicPostRepository.findById(schematicPostId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage())
				);
		
		if(schematic.getMember().getId() != user.getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		schematicPostRepository.delete(schematic);
		
		return true;
	}

}
