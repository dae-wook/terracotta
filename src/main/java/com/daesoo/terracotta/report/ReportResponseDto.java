package com.daesoo.terracotta.report;

import java.time.LocalDateTime;

import com.daesoo.terracotta.common.entity.Comment;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.Reply;
import com.daesoo.terracotta.common.entity.Report;
import com.daesoo.terracotta.common.entity.SchematicPost;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportResponseDto {

    private Long id;

    private String reason;

    private Member reportedBy;

    private SchematicPost schematicPost;

    private Comment comment;
    
    private Reply reply;
    
    private LocalDateTime createdAt;
    
    public static ReportResponseDto of(Report report) {
    	return ReportResponseDto.builder()
    			.id(report.getId())
    			.reason(report.getReason())
    			.reportedBy(report.getReportedBy())
    			.schematicPost(report.getSchematicPost())
    			.comment(report.getComment())
    			.reply(report.getReply())
    			.createdAt(report.getCreatedAt())
    			.build();
    }
	
}
