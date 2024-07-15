package com.daesoo.terracotta.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{

}
