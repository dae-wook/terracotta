package com.daesoo.terracotta.common.entity;

import java.util.ArrayList;
import java.util.List;

import com.daesoo.terracotta.post.dto.SchematicPostRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "schematic_posts")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchematicPost extends TimeStamp{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    private String image;
    
    private int price;
    
    private int buyCount;
    
    private int commentCount;
    
    private float totalStar;
    
    private float star;

    @OneToMany(mappedBy = "schematicPost", cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();
    
    @OneToMany(mappedBy = "schematicPost", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    
    @ManyToOne
    private Member member;
    
    public static SchematicPost create(SchematicPostRequestDto dto, String[] filePath, Member member) {
    	return SchematicPost.builder()
    			.title(dto.getTitle())
    			.content(dto.getDescription())
    			.filePath(filePath[0])
    			.image(filePath[1])
    			.member(member)
    			.postTags(new ArrayList<PostTag>())
    			.build();
    }
    
    public void addPostTag(Tag tag) {
        PostTag postTag = PostTag.create(this, tag);
        postTags.add(postTag);
        tag.getPostTags().add(postTag);
    }
    
    public void addPostTags(List<Tag> tags) {
    	postTags.clear();
    	for(Tag tag : tags) {
    		PostTag postTag = PostTag.create(this, tag);
    		postTags.add(postTag);
    		
    		tag.getPostTags().add(postTag);
    	}
    }
    
    public void addComment(Comment comment) {
    	this.commentCount++;
    	this.totalStar += comment.getStar();
    	this.star = averageStar();
    }

	public void deleteComment(Comment comment) {
		this.commentCount--;
    	this.totalStar -= comment.getStar();
    	this.star = averageStar();
		
	}

	public void updateComment(Comment comment, float oldStar) {
		// TODO Auto-generated method stub
		this.totalStar -= oldStar;
    	this.totalStar += comment.getStar();
    	this.star = averageStar();
		
	}

	public void update(SchematicPostRequestDto schematicPostRequestDto, String[] filePath) {

		this.title = schematicPostRequestDto.getTitle() != null ? schematicPostRequestDto.getTitle() : this.title;
		this.content = schematicPostRequestDto.getDescription() != null ? schematicPostRequestDto.getDescription() : this.content;
		this.price = schematicPostRequestDto.getPrice() != null ? schematicPostRequestDto.getPrice() : this.price;
		this.filePath = filePath[0] != null ? filePath[0] : this.filePath;
		this.image = filePath[1] != null ? filePath[1] : this.image;
		
	}
	
	public float averageStar() {
		return Math.round((this.totalStar * 1.0f / this.commentCount) * 10) / 10.0f;
	}

}
