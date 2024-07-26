package com.daesoo.terracotta.image;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.common.exception.UnauthorizedException;
import com.daesoo.terracotta.member.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3737", "https://terracotta-nu.vercel.app"})
public class ImageController {
	
	private final ImageService imageService;

	@PostMapping
	public ResponseDto<UploadedImageResponseDto> uploadImages(
			@RequestParam("images") MultipartFile[] images,
			@RequestParam("type") ImageType imageType,
	        @AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		if (userDetails == null) {
	        throw new UnauthorizedException(ErrorMessage.UNAHTHORIZED.getMessage());
	    }
		return ResponseDto.success(HttpStatus.OK, imageService.uploadImages(images, imageType, userDetails.getUser()));
	}
	
	@DeleteMapping
	public ResponseDto<Boolean> deleteImages(@RequestParam("imageNames") String[] imageNames) {
		return ResponseDto.success(HttpStatus.OK, imageService.deleteImages(imageNames));
	}
	
	@DeleteMapping("/all")
	public ResponseDto<Integer> deleteImagesExcept() {
		return ResponseDto.success(HttpStatus.OK, imageService.deleteFilesExcept("image"));
	}
	
	@DeleteMapping("/alls")
	public ResponseDto<Integer> deleteSchematicsExcept() {
		return ResponseDto.success(HttpStatus.OK, imageService.deleteFilesExcept("schematic"));
	}
	
}