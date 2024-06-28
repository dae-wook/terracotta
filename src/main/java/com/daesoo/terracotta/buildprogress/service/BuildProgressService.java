package com.daesoo.terracotta.buildprogress.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.buildprogress.dto.BuildProgressInScheamticPostResponseDto;
import com.daesoo.terracotta.buildprogress.dto.BuildProgressRequestDto;
import com.daesoo.terracotta.buildprogress.dto.BuildProgressResponseDto;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.repository.BuildProgressRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;
import com.daesoo.terracotta.post.dto.SchematicPostResponseDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildProgressService {
	
	private final BuildProgressRepository buildProgressRepository;
	private final SchematicPostRepository schematicPostRepository;

	@Transactional
	public BuildProgressResponseDto createBuildProgress(Member member,
			Long schematicPostId) {

		SchematicPost schematicPost = schematicPostRepository.findById(schematicPostId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
		
		if(schematicPost.getMember().equals(member)) {
			throw new IllegalArgumentException(ErrorMessage.NO_PERMISSION.getMessage());
		}
		
		if(buildProgressRepository.findBySchematicPostAndMember(schematicPost, member).isPresent()) {
			throw new IllegalArgumentException(ErrorMessage.DUPLICATE_REQUEST.getMessage());
		}
		
		BuildProgress buildProgress = BuildProgress.create(member, schematicPost);
		
		
		schematicPost.increaseBuyCount();
		
		buildProgressRepository.save(buildProgress);
		
		
		
		return BuildProgressResponseDto.of(buildProgress);
	}

	
	public Page<SchematicPostResponseDto> getBuildProgressListByLoginMember(Member member, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		buildProgressRepository.findAllByMember(pageable, member).map(SchematicPostResponseDto :: of);
//		return buildProgressRepository.findAllByMember(pageable, member).map(BuildProgressResponseDto :: of);
		return buildProgressRepository.findAllByMember(pageable, member).map(SchematicPostResponseDto :: of);
	}

	@Transactional
	public BuildProgressInScheamticPostResponseDto updateBuildProgress(Member user, Long buildProgressId, BuildProgressRequestDto dto) {
		
		BuildProgress buildProgress = buildProgressRepository.findById(buildProgressId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
		
		if (user.getId() != buildProgress.getMember().getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		
		buildProgress.update(dto);
		
		
		return BuildProgressInScheamticPostResponseDto.of(buildProgress);
	}


	@Transactional
	public Boolean deleteBuildProgress(Member user, Long buildProgressId) {
		
		BuildProgress buildProgress = buildProgressRepository.findById(buildProgressId).orElseThrow(
				() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage()));
		
		if (user.getId() != buildProgress.getMember().getId()) {
			throw new IllegalArgumentException(ErrorMessage.ACCESS_DENIED.getMessage());
		}
		
		buildProgress.getSchematicPost().decreaseBuyCount();
		
		buildProgressRepository.delete(buildProgress);
		
		return true;
	}
}













