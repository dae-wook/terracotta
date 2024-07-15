package com.daesoo.terracotta.report;

import org.springframework.stereotype.Service;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.Comment;
import com.daesoo.terracotta.common.entity.Member;
import com.daesoo.terracotta.common.entity.Reply;
import com.daesoo.terracotta.common.entity.Report;
import com.daesoo.terracotta.common.entity.SchematicPost;
import com.daesoo.terracotta.common.repository.CommentRepository;
import com.daesoo.terracotta.common.repository.ReplyRepository;
import com.daesoo.terracotta.common.repository.ReportRepository;
import com.daesoo.terracotta.common.repository.SchematicPostRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final ReportRepository reportRepository;
    private final SchematicPostRepository schematicPostRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
	
    public ReportResponseDto reportPost(Long postId, String reason, Member member) {
        SchematicPost post = schematicPostRepository.findById(postId).orElseThrow(
        		() -> new EntityNotFoundException(ErrorMessage.POST_NOT_FOUND.getMessage())
        		);

        Report report = Report.builder()
                .schematicPost(post)
                .reportedBy(member)
                .reason(reason)
                .build();
        
        reportRepository.save(report);
        
        return ReportResponseDto.of(report);
    }
    
    public ReportResponseDto reportComment(Long commentId, String reason, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
        		() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
        		);

        Report report = Report.builder()
                .comment(comment)
                .reportedBy(member)
                .reason(reason)
                .build();
        
        reportRepository.save(report);
        
        return ReportResponseDto.of(report);
    }
    
    public ReportResponseDto reportReply(Long replyId, String reason, Member member) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(
        		() -> new EntityNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage())
        		);

        Report report = Report.builder()
                .reply(reply)
                .reportedBy(member)
                .reason(reason)
                .build();
        
        reportRepository.save(report);
        
        return ReportResponseDto.of(report);
    }

}
