package com.daesoo.terracotta.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.SchematicPost;

public interface SchematicPostRepository extends JpaRepository<SchematicPost, Long>{

	Page<SchematicPost> findAllByMember(Pageable pageable, Member user);
	
	

}
