package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.comment.dto.CommentRequestDto;

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

@Entity(name = "comments")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends TimeStamp{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    private float star;
    
    @ManyToOne
    private Member member;
    
    @ManyToOne
    private SchematicPost schematicPost;
    
    public static Comment create(CommentRequestDto dto, SchematicPost schematicPost, Member member) {
    	return Comment.builder()
    			.content(dto.getContent())
    			.star(dto.getStar())
    			.member(member)
    			.schematicPost(schematicPost)
    			.build();
    }

	public void update(CommentRequestDto dto) {
		this.content = dto.getContent();
		this.star = dto.getStar();
		
	}

}
