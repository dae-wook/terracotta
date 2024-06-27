package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.comment.dto.ReplyRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "replies")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends TimeStamp{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false)
    private String content;
    
    @ManyToOne
    private Member member;
    
    @ManyToOne
    private Comment comment;
    
    public static Reply create(ReplyRequestDto dto, Comment comment, Member member) {
    	return Reply.builder()
    			.content(dto.getContent())
    			.comment(comment)
    			.member(member)
    			.build();
    }

	public void update(ReplyRequestDto dto) {
		this.content = dto.getContent();
		
	}

}
