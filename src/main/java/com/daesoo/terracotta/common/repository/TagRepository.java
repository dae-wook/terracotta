package com.daesoo.terracotta.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{

}
