package com.daesoo.terracotta.report;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.exception.UnauthorizedException;
import com.daesoo.terracotta.member.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3737", "https://terracotta-nu.vercel.app"})
@Slf4j
public class ReportContoller {
	
	
	private final ReportService reportService;
	
	@PostMapping("/posts/{postId}")
    public ResponseDto<ReportResponseDto> reportPost(@PathVariable Long postId,  
                                             @RequestParam String reason,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
        return ResponseDto.success(HttpStatus.CREATED, reportService.reportPost(postId, reason, userDetails.getUser()));
    }

    @PostMapping("/comments/{commentId}")
    public ResponseDto<ReportResponseDto> reportComment(@PathVariable Long commentId, 
								            @RequestParam String reason,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
    	
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
    	
		return ResponseDto.success(HttpStatus.CREATED, reportService.reportComment(commentId, reason, userDetails.getUser()));
    }
    
    @PostMapping("/replies/{replyId}")
    public ResponseDto<ReportResponseDto> reportReply(@PathVariable Long replyId, 
								            @RequestParam String reason,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
    	
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
    	
		return ResponseDto.success(HttpStatus.CREATED, reportService.reportReply(replyId, reason, userDetails.getUser()));
    }
    
	@PutMapping("/{reportId}")
    public ResponseDto<ReportResponseDto> deleteReport(@PathVariable Long reportId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		
        return ResponseDto.success(HttpStatus.CREATED, reportService.updateReportStatus(reportId, userDetails.getUser()));
    }



}
