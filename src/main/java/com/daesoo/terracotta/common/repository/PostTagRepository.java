package com.daesoo.terracotta.common.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.PostTag;
import com.daesoo.terracotta.common.entity.SchematicPost;

public interface PostTagRepository extends JpaRepository<PostTag, Long>{

    
    Page<PostTag> findAllByTagTagIdIn(Pageable pageable, List<Long> tagIds);
    
    
    @Query("select distinct pt.schematicPost from post_tags pt where pt.tag.id in :tagIds")
    Page<SchematicPost> findPostsByTags(Pageable pageable, @Param("tagIds") Long[] tagIds);
    
    @Query("select distinct pt.schematicPost from post_tags pt where pt.tag.id in :tagIds group by pt.schematicPost having count(distinct pt.tag) = :tagCount")
    Page<SchematicPost> findPostsByTags(Pageable pageable, @Param("tagIds") Long[] tagIds, @Param("tagCount") int tagCount);


	List<PostTag> findAllBySchematicPostId(Long schematicPostId);

	@Query("select distinct pt.schematicPost from post_tags pt where pt.tag.id in :tagIds and pt.schematicPost.member = :member group by pt.schematicPost having count(distinct pt.tag) = :tagCount")
	Page<SchematicPost> findPostsByTagsAndMember(Pageable pageable, @Param("tagIds") Long[] tags, @Param("tagCount") int length, @Param("member")Member member);

	
}
