package com.daesoo.terracotta.common.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.daesoo.terracotta.post.dto.FileNameDto;
import com.daesoo.terracotta.post.dto.SchematicPostRequestDto;
import com.daesoo.terracotta.schematic.util.SchematicDto;

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
    
    private String image;
    
    private int price;
    
    private int buyCount;
    
    private int commentCount;
    
    private int size;
    
    private float totalStar;
    
    private float star;

    @OneToMany(mappedBy = "schematicPost", cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();
    
    @OneToMany(mappedBy = "schematicPost", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    
    @OneToMany(mappedBy = "schematicPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
    
    @OneToMany(mappedBy = "schematicPost", cascade = CascadeType.ALL)
    private List<BuildProgress> buildProgress = new ArrayList<>();
    
    @ManyToOne
    private Member member;
    
    public static SchematicPost create(SchematicPostRequestDto dto, SchematicDto schematicDto, FileNameDto fileName, Member member) {
    	return SchematicPost.builder()
    			.title(dto.getTitle())
    			.content(dto.getDescription())
    			.size(schematicDto.getSize())
    			.filePath(fileName.getSchameticName())
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

	public void update(SchematicPostRequestDto schematicPostRequestDto, SchematicDto schematicDto, String[] filePath) {

		this.title = schematicPostRequestDto.getTitle() != null ? schematicPostRequestDto.getTitle() : this.title;
		this.content = schematicPostRequestDto.getDescription() != null ? schematicPostRequestDto.getDescription() : this.content;
		this.price = schematicPostRequestDto.getPrice() != null ? schematicPostRequestDto.getPrice() : this.price;
		this.filePath = filePath[0] != null ? filePath[0] : this.filePath;
		this.size = schematicDto != null ? schematicDto.getSize() : this.size;
		this.image = filePath[1] != null ? filePath[1] : this.image;
		
	}
	
	private float averageStar() {
		return Math.round((this.totalStar * 1.0f / this.commentCount) * 10) / 10.0f;
	}

	public void addImages(String[] imageNames) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }

        Iterator<Image> iterator = this.images.iterator();
        while (iterator.hasNext()) {
            Image image = iterator.next();
            image.setSchematicPost(null);
            iterator.remove();
        }

        for (String imageName : imageNames) {
            Image image = Image.create(this, imageName);
            this.images.add(image);
        }
    }

}
