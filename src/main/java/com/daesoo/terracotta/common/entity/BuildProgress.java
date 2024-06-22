package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.buildprogress.dto.BuildProgressRequestDto;

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

@Entity(name = "build_progress")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildProgress extends TimeStamp{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/* camera: {
		   position:  x y z,
		   target:  x y z
		}
		hiddenMaterials: string 배열
		visibleRange: {
		   start: x y z,
		   end: x y z
	} */
	
	@Column(length = 500)
	private String setting;
    
    
    @ManyToOne
    private Member member;
    
    @ManyToOne
    private SchematicPost schematicPost;
    
    public static BuildProgress create(Member member, SchematicPost schematicPost) {
    	return BuildProgress.builder()
    			.setting(null)
    			.member(member)
    			.schematicPost(schematicPost)
    			.build();
    			
    }
    
    public void update(BuildProgressRequestDto dto) {
    	this.setting = dto.getSetting();
    }
    


}
