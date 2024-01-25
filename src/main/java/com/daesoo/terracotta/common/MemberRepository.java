package com.daesoo.terracotta.common;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daesoo.terracotta.common.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Optional<Member> findByUserId(String userId);
	Optional<Member> findByUsername(String username);
	Optional<Member> findByEmail(String Email);
	

}
