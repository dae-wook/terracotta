package com.daesoo.terracotta.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{

}
