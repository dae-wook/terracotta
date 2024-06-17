package com.daesoo.terracotta.common.entity;

import java.util.ArrayList;
import java.util.List;

import com.daesoo.terracotta.member.dto.SignupRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String memberName;
    
    
    public static Member create(SignupRequestDto signupRequestDto, String encodedPassword) {
    	return Member.builder()
    			.email(signupRequestDto.getEmail())
    			.password(encodedPassword)
    			.memberName(signupRequestDto.getNickname())
    			.build();
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SchematicPost> schematicPosts;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notifications;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<BuildProgress> buildProgress = new ArrayList<>();

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
