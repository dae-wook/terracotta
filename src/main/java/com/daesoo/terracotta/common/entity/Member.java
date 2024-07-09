package com.daesoo.terracotta.common.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.daesoo.terracotta.member.dto.SignupRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    
    private String profileImage;
    
    @Column(length = 500)
    private String introduction;
    
    private String passwordResetKey;

    private LocalDateTime passwordResetExpiry;
    
    
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
    


	public LocalDateTime passwordResetSetting(String key) {
		this.passwordResetKey = key;
		this.passwordResetExpiry = LocalDateTime.now().plusMinutes(5);
		return this.passwordResetExpiry;
		
	}


	public void resetPassword(String encodedPassword) {
		this.password = encodedPassword;
	}
	
	public void clearResetPasswordInfo() {
		this.passwordResetKey = null;
		this.passwordResetExpiry = null;
	}
	
	public void updateIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public void updateProfileImage(String fileName) {
		this.profileImage = fileName;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
