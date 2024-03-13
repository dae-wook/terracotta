package com.daesoo.terracotta.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{


}
