package com.daesoo.terracotta.common.entity;

import com.daesoo.terracotta.buildprogress.dto.BuildProgressRequestDto;

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
	
	private String cameraPosition;
	private String cameraTarget;
	private String hiddenMateriacls;
	private String visibleRangeStart;
	private String visibleRangeEnd;
    
    
    @ManyToOne
    private Member member;
    
    @ManyToOne
    private SchematicPost schematicPost;
    
    public static BuildProgress create(Member member, SchematicPost schematicPost) {
    	return BuildProgress.builder()
    			.cameraPosition(null)
    			.cameraTarget(null)
    			.hiddenMateriacls(null)
    			.visibleRangeStart(null)
    			.visibleRangeEnd(null)
    			.member(member)
    			.schematicPost(schematicPost)
    			.build();
    			
    }
    
    public void update(BuildProgressRequestDto dto) {
    	this.cameraPosition = dto.getCameraPosition();
    	this.cameraTarget = dto.getCameraTarget();
    	this.hiddenMateriacls = dto.getHiddenMaterials();
    	this.visibleRangeStart = dto.getVisibleRangeStart();
    	this.visibleRangeEnd = dto.getVisibleRangeEnd();
    }
    


}
