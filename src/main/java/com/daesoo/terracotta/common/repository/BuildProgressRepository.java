package com.daesoo.terracotta.common.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.BuildProgress;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;

public interface BuildProgressRepository extends JpaRepository<BuildProgress, Long>{

	Page<BuildProgress> findAllByMember(Pageable pageable, Member member);

	Optional<BuildProgress> findBySchematicPostAndMember(SchematicPost schematicPost, Member member);

}
