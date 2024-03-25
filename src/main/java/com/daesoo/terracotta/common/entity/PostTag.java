package com.daesoo.terracotta.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "post_tags")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostTag extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schematic_post_id")
    private SchematicPost schematicPost;

    @ManyToOne
    @JoinColumn(name ="tag_id")
    private Tag tag;
    
    public static PostTag create(SchematicPost schematicPost, Tag tag) {
    	return PostTag.builder()
    			.schematicPost(schematicPost)
    			.tag(tag)
    			.build();
    }

}
