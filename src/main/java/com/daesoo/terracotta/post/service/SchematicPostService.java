package com.daesoo.terracotta.post.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.PostTag;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.entity.Tag;
import com.daesoo.terracotta.common.repository.MemberRepository;
import com.daesoo.terracotta.common.repository.PostTagRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.common.repository.TagRepository;
import com.daesoo.terracotta.common.util.FileUtil;
import com.daesoo.terracotta.post.dto.FileNameDto;
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
	private final MemberRepository memberRepository;
	private final FileUtil fileUtil;
	private final SchemeParser schemParser;
	

	
	@Transactional
	public SchematicPostResponseDto createSchematicPost(SchematicPostRequestDto schematicPostRequestDto, Member member) {
		

		SchematicDto schematicDto = new SchematicDto();
		String originalFilename = schematicPostRequestDto.getFile().getOriginalFilename();
		try {
			schematicDto = schemParser.convertFileToSchematicJson(schematicPostRequestDto.getFile().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		FileNameDto fileName = fileUtil.uploadFile(schematicDto, schematicPostRequestDto.getFile(), schematicPostRequestDto.getImages());
		
		SchematicPost schematicPost = SchematicPost.create(schematicPostRequestDto, schematicDto, fileName, member);
		
		List<Tag> tags = tagRepository.findAllById(schematicPostRequestDto.getTags());
		for(Tag tag: tags) {
			schematicPost.addPostTag(tag);
		}
		
		schematicPost.addImages(fileName.getImageNames());
		
		schematicPostRepository.save(schematicPost);
		
		return SchematicPostResponseDto.of(schematicPost);
	}
	
	@Transactional
	public SchematicPostResponseDto updateSchematicPost(Long schematicPostId, SchematicPostRequestDto schematicPostRequestDto, Member member) {
		
		SchematicPost schematicPost = schematicPostRepository.findById(schematicPostId)
				.orElseThrow( () -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
		
		SchematicDto schematicDto = null;
		
		
		if(schematicPost.getMember().getId() != member.getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		String[] filePath = new String[2];
		if(schematicPostRequestDto.getFile() != null) {
			try {
				schematicDto = schemParser.convertFileToSchematicJson(schematicPostRequestDto.getFile().getBytes());
				filePath[0] = fileUtil.updateSchematic(schematicDto, schematicPostRequestDto.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(schematicPostRequestDto.getImages() != null) {
			FileNameDto fileNameDto = fileUtil.updateImage(schematicPostRequestDto.getImages(), schematicPost.getImages());

			schematicPost.addImages(fileNameDto.getImageNames());
		}
		
		schematicPost.update(schematicPostRequestDto, schematicDto, filePath);
		
		
		List<PostTag> postTags = postTagRepository.findAllBySchematicPostId(schematicPostId);
		
		
		List<Tag> tags = tagRepository.findAllById(schematicPostRequestDto.getTags());
		if (!compareTag(postTags, tags)) {
			postTagRepository.deleteAll(postTags);
			schematicPost.addPostTags(tags);
		}
		
		
		return SchematicPostResponseDto.of(schematicPost);
	}


	private boolean compareTag(List<PostTag> postTags, List<Tag> tags) {

		if(postTags.size() != tags.size()) return false;
		
	    // 포스트 태그의 ID를 저장할 셋을 생성
	    Set<Long> postTagIds = new HashSet<>();
	    for (PostTag postTag : postTags) {
	        postTagIds.add(postTag.getTag().getTagId());
	    }

	    // 비교할 태그 리스트를 순회하면서 포스트 태그의 ID와 비교
	    for (Tag tag : tags) {
	        if (!postTagIds.contains(tag.getTagId())) {
	            // 포스트 태그의 ID와 일치하는 태그가 없으면 false 반환
	            return false;
	        }
	    }
		
		return true;
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


	public Page<SchematicPostResponseDto> getSchematicPostList(Integer page, Integer size, Long[] tags) {
		
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		if(tags[0] == 0) {
			return schematicPostRepository.findAll(pageable).map(SchematicPostResponseDto::of);
		}

		return postTagRepository.findPostsByTags(pageable, tags, tags.length).map(SchematicPostResponseDto::of);
	}
	


	public Page<SchematicPostResponseDto> getSchematicPostListByMemberName(String memberName, Integer page, Integer size) {
		
		Member member = memberRepository.findByMemberName(memberName).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
		
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		return schematicPostRepository.findAllByMember(pageable, member).map(SchematicPostResponseDto::of);

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
