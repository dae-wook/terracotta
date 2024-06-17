package com.daesoo.terracotta.buildprogress.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.daesoo.terracotta.buildprogress.dto.BuildProgressResponseDto;
import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.repository.BuildProgressRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;

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
		
		BuildProgress buildProgress = BuildProgress.create(member, schematicPost);
		
		buildProgressRepository.save(buildProgress);
		
		
		
		return BuildProgressResponseDto.of(buildProgress);
	}

	
	public Page<BuildProgressResponseDto> getBuildProgressListByLoginMember(Member member, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		return buildProgressRepository.findAllByMember(pageable, member).map(BuildProgressResponseDto :: of);
	}
}













