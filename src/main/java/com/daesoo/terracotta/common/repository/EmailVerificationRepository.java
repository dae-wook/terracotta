package com.daesoo.terracotta.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daesoo.terracotta.common.entity.EmailVerification;
import com.daesoo.terracotta.common.entity.Member;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long>{

	Optional<EmailVerification> findByEmailAndAuthCode(String email, String authCode);

	Optional<EmailVerification> findByEmail(String email);

	List<EmailVerification> findAllByEmail(String email);

}
