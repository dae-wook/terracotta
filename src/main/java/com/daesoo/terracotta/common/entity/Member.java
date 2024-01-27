package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.member.dto.SignupRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "members")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends TimeStamp{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "is_active", columnDefinition = "boolean default false")
    private boolean isActive;
    
    public static Member create(SignupRequestDto signupRequestDto, String encodedPassword) {
    	return Member.builder()
    			.userId(signupRequestDto.getUserId())
    			.password(encodedPassword)
    			.username(signupRequestDto.getUsername())
    			.email(signupRequestDto.getEmail())
    			.build();
    }

//    @OneToMany(mappedBy = "member")
//    private Set<BoardWishMember> wishes;

//    public static Member create(SignupRequestDto signupRequestDto, String encodedPassword) {
//        return Member.builder()
//                .userId(signupRequestDto.getUserId())
//                .username(signupRequestDto.getUsername())
//                .email(signupRequestDto.getEmail())
//                .password(encodedPassword)
//                .gender(signupRequestDto.getGender())
//                .birthday(signupRequestDto.getBirthday())
//                .build();
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

//    @PreRemove
//    private void memberIdSetNullAtBoardWish() {
//        this.getWishes().forEach(BoardWishMember::setMemberNull);
//    }
}
