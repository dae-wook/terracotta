package com.daesoo.terracotta.common.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

	Page<Notification> findAllByMember(Pageable pageable, Member user);

	List<Notification> findAllByIsReadTrueAndMember(Member user);
	
	int deleteByIsReadTrueAndMember(Member member);


}
