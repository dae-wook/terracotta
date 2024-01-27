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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity(name = "schematic_posts")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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

    @OneToMany(mappedBy = "schematicPost", cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();
    
    @ManyToOne
    private Member member;
    
    public static SchematicPost create(SchematicPostRequestDto dto, String filePath, Member member) {
    	return SchematicPost.builder()
    			.title(dto.getTitle())
    			.content(dto.getContent())
    			.filePath(filePath)
    			.member(member)
    			.postTags(new ArrayList<PostTag>())
    			.build();
    }
    
    public void addPostTag(Tag tag) {
        PostTag postTag = PostTag.create(this, tag);
        postTags.add(postTag);
        tag.getPostTags().add(postTag);
    }

}
