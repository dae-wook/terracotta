package com.daesoo.terracotta.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daesoo.terracotta.common.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Optional<Member> findByEmail(String email);
	Optional<Member> findByMemberName(String memberName);
	Optional<Member> findByPasswordResetKey(String key);
	
}
