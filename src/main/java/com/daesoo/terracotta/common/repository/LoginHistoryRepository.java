package com.daesoo.terracotta.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long>{
	
	Page<LoginHistory> findAllByEmail(Pageable pageable, String email);

}
